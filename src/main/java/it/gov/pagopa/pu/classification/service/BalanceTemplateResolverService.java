package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.dto.generated.CalculateAmountBalanceRequest;
import it.gov.pagopa.pu.classification.dto.generated.DebtPositionTypeOrgBalanceCostDTO;
import it.gov.pagopa.pu.classification.enums.BalanceDefaultAmountType;
import it.gov.pagopa.pu.classification.exception.custom.InvalidValueException;
import it.gov.pagopa.pu.classification.util.ErrorCodeConstants;
import it.gov.pagopa.pu.classification.util.Utilities;
import it.veneto.regione.schemas._2012.pagamenti.ente.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.math.BigDecimal;

@Service
@Slf4j
public class BalanceTemplateResolverService {
  private static final String DEFAULT_FALLBACK_CODE = "SEND";

  private final BalanceService balanceService;
  private final BalanceDefaultMarshallingService balanceDefaultMarshallingService;
  private final BalanceMarshallingService balanceMarshallingService;
  private final ScriptEngineManager factory = new ScriptEngineManager();
  private final ScriptEngine engine = factory.getEngineByName("rhino");

  public BalanceTemplateResolverService(
    BalanceService balanceService,
    BalanceDefaultMarshallingService balanceDefaultMarshallingService,
    BalanceMarshallingService balanceMarshallingService
  ) {
    this.balanceService = balanceService;
    this.balanceDefaultMarshallingService = balanceDefaultMarshallingService;
    this.balanceMarshallingService = balanceMarshallingService;
  }

  public String calculateAmountBalance(CalculateAmountBalanceRequest calculateAmountBalanceRequest) {
    String balance = calculateAmountBalanceRequest.getBalance();
    BigDecimal amountInstallment = Utilities.longCentsToBigDecimalEuro(calculateAmountBalanceRequest.getAmountCents());
    Long notificationFeeCents = calculateAmountBalanceRequest.getNotificationFeeCents();

    Object balanceXML = balanceService.unmarshalBalance(balance, null);

    if (balanceXML instanceof CtBilancio) {
      log.info("The balance amount is already calculated");
      if (notificationFeeCents != null && notificationFeeCents != 0) {
        addNotificationCostToBalance((CtBilancio) balanceXML, calculateAmountBalanceRequest, notificationFeeCents);
        return balanceMarshallingService.marshal((CtBilancio) balanceXML);
      }

      return balance;
    }

    try {
      CtBilancioDefault ctBilancioDefault = (CtBilancioDefault) balanceXML;
      log.info("Calculating balance amount resolving default type");
      for (CtCapitoloDefault capitolo : ctBilancioDefault.getCapitolo()) {
        for (CtAccertamentoDefault ctAccertamentoDefault : capitolo.getAccertamento()) {
          BigDecimal calculatedAmount;
          if (ctAccertamentoDefault.getImporto().equals(BalanceDefaultAmountType.TOTAL.getType())) {
            calculatedAmount = amountInstallment;
          } else if (ctAccertamentoDefault.getImporto().contains(BalanceDefaultAmountType.EXTRACT_AMOUNT.getType())) {
            engine.eval(ctAccertamentoDefault.getImporto());
            Invocable invocable = (Invocable) engine;
            String result = String.valueOf(invocable.invokeFunction(BalanceDefaultAmountType.EXTRACT_AMOUNT.getType(),
              calculateAmountBalanceRequest.getRemittanceInformation()));
            calculatedAmount = new BigDecimal(result);
          } else if (ctAccertamentoDefault.getImporto().contains(BalanceDefaultAmountType.CALCULATE_AMOUNT.getType())) {
            engine.eval(ctAccertamentoDefault.getImporto());
            Invocable invocable = (Invocable) engine;
            String result = String.valueOf(
              invocable.invokeFunction(BalanceDefaultAmountType.CALCULATE_AMOUNT.getType(), amountInstallment));
            calculatedAmount = new BigDecimal(result);
          } else {
            throw new InvalidValueException(ErrorCodeConstants.ERROR_CODE_BALANCE_CALCULATION_ERROR, ctAccertamentoDefault.getImporto() + " as function type to calculate amount balance not supported");
          }
          String amountString = Utilities.amountToString(calculatedAmount);

          ctAccertamentoDefault.setImporto(amountString);
        }
      }

      if (notificationFeeCents != null && notificationFeeCents != 0) {
        addNotificationCostToBalanceDefault(ctBilancioDefault, calculateAmountBalanceRequest, notificationFeeCents);
      }

      return balanceDefaultMarshallingService.marshal(ctBilancioDefault);
    } catch (Exception e) {
      throw new InvalidValueException(ErrorCodeConstants.ERROR_CODE_BALANCE_CALCULATION_ERROR, "Error calculating amount of balance: " + e.getMessage());
    }
  }

  private void addNotificationCostToBalanceDefault(CtBilancioDefault ctBilancioDefault, CalculateAmountBalanceRequest request, Long notificationFeeCents) {
    log.info("Adding notification costs to the balance");
    NotificationCodes codes = extractNotificationCodes(request.getDebtPositionTypeOrgBalanceCost());

    CtCapitoloDefault capitoloNotifica = new CtCapitoloDefault();
    capitoloNotifica.setCodCapitolo(codes.sectionCode());
    capitoloNotifica.setCodUfficio(codes.officeCode());

    CtAccertamentoDefault accertamentoNotifica = new CtAccertamentoDefault();
    accertamentoNotifica.setCodAccertamento(codes.assessmentCode());

    BigDecimal notificationAmount = Utilities.longCentsToBigDecimalEuro(notificationFeeCents);
    accertamentoNotifica.setImporto(Utilities.amountToString(notificationAmount));

    capitoloNotifica.getAccertamento().add(accertamentoNotifica);
    ctBilancioDefault.getCapitolo().add(capitoloNotifica);
  }

  private void addNotificationCostToBalance(CtBilancio ctBilancio, CalculateAmountBalanceRequest request, Long notificationFeeCents) {
    log.info("Adding notification costs to the balance");
    NotificationCodes codes = extractNotificationCodes(request.getDebtPositionTypeOrgBalanceCost());

    CtCapitolo capitoloNotifica = new CtCapitolo();
    capitoloNotifica.setCodCapitolo(codes.sectionCode());
    capitoloNotifica.setCodUfficio(codes.officeCode());

    CtAccertamento accertamentoNotifica = new CtAccertamento();
    accertamentoNotifica.setCodAccertamento(codes.assessmentCode());

    BigDecimal notificationAmount = Utilities.longCentsToBigDecimalEuro(notificationFeeCents);
    accertamentoNotifica.setImporto(notificationAmount);

    capitoloNotifica.getAccertamento().add(accertamentoNotifica);
    ctBilancio.getCapitolo().add(capitoloNotifica);
  }

  private record NotificationCodes(
    String officeCode,
    String sectionCode,
    String assessmentCode
  ) {}

  private NotificationCodes extractNotificationCodes(DebtPositionTypeOrgBalanceCostDTO dto) {
    if (dto != null) {
      return new NotificationCodes(dto.getOfficeCode(), dto.getSectionCode(), dto.getAssessmentCode());
    }

    return new NotificationCodes(DEFAULT_FALLBACK_CODE, DEFAULT_FALLBACK_CODE, DEFAULT_FALLBACK_CODE);
  }
}

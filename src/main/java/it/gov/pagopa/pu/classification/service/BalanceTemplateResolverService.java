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
import javax.script.ScriptException;
import java.math.BigDecimal;

import static it.gov.pagopa.pu.classification.util.Constants.DEFAULT_SEND_DPTOBC_CODE;

@Service
@Slf4j
public class BalanceTemplateResolverService {
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
    Long notificationFeeCents = calculateAmountBalanceRequest.getNotificationFeeCents();

    Object balanceXML = balanceService.unmarshalBalance(balance, null);

    if (balanceXML instanceof Bilancio ctBilancio) {
      log.info("The balance amount is already calculated");
      if (notificationFeeCents != null && notificationFeeCents != 0) {
        addNotificationCostToBalance(ctBilancio, calculateAmountBalanceRequest, notificationFeeCents);
        return balanceMarshallingService.marshal(ctBilancio);
      }

      return balance;
    }
    if (balanceXML instanceof BilancioDefault ctBilancioDefault) {
      return processAndMarshalDefaultBalance(ctBilancioDefault, calculateAmountBalanceRequest);
    }
    throw new InvalidValueException(ErrorCodeConstants.ERROR_CODE_BALANCE_MARSHALLING_ERROR, "Unsupported balance structure type");
  }

  public String processAndMarshalDefaultBalance(BilancioDefault ctBilancioDefault, CalculateAmountBalanceRequest calculateAmountBalanceRequest) {
    try {
      log.info("Calculating balance amount resolving default type");
      BigDecimal amountInstallment = Utilities.longCentsToBigDecimalEuro(calculateAmountBalanceRequest.getAmountCents());
      Long notificationFeeCents = calculateAmountBalanceRequest.getNotificationFeeCents();
      for (CtCapitoloDefault capitolo : ctBilancioDefault.getCapitolos()) {
        for (CtAccertamentoDefault ctAccertamentoDefault : capitolo.getAccertamentos()) {
          BigDecimal calculatedAmount = calculateAccertamentoAmount(ctAccertamentoDefault, amountInstallment, calculateAmountBalanceRequest);
          ctAccertamentoDefault.setImporto(Utilities.amountToString(calculatedAmount));
        }
      }

      if (notificationFeeCents != null && notificationFeeCents != 0) {
        addNotificationCostToBalanceDefault(ctBilancioDefault, calculateAmountBalanceRequest, notificationFeeCents);
      }

      return balanceDefaultMarshallingService.marshal(ctBilancioDefault);
    } catch (Exception e) {
      log.error("Failed to calculate default balance amounts", e);
      throw new InvalidValueException(ErrorCodeConstants.ERROR_CODE_BALANCE_CALCULATION_ERROR, "Error calculating amount of balance: " + e.getMessage());
    }
  }

  private BigDecimal calculateAccertamentoAmount(CtAccertamentoDefault accertamento, BigDecimal amountInstallment, CalculateAmountBalanceRequest request) throws ScriptException, NoSuchMethodException, NumberFormatException {
    String importo = accertamento.getImporto();

    if (importo.equals(BalanceDefaultAmountType.TOTAL.getType())) {
      return amountInstallment;
    }

    if (importo.contains(BalanceDefaultAmountType.EXTRACT_AMOUNT.getType())) {
      engine.eval(importo);
      Invocable invocable = (Invocable) engine;
      String result = String.valueOf(invocable.invokeFunction(BalanceDefaultAmountType.EXTRACT_AMOUNT.getType(), request.getRemittanceInformation()));
      return new BigDecimal(result);
    }

    if (importo.contains(BalanceDefaultAmountType.CALCULATE_AMOUNT.getType())) {
      engine.eval(importo);
      Invocable invocable = (Invocable) engine;
      String result = String.valueOf(invocable.invokeFunction(BalanceDefaultAmountType.CALCULATE_AMOUNT.getType(), amountInstallment));
      return new BigDecimal(result);
    }

    throw new InvalidValueException(ErrorCodeConstants.ERROR_CODE_BALANCE_CALCULATION_ERROR, importo + " as function type to calculate amount balance not supported");
  }

  private void addNotificationCostToBalanceDefault(BilancioDefault ctBilancioDefault, CalculateAmountBalanceRequest request, Long notificationFeeCents) {
    log.info("Adding notification costs to the balance");
    NotificationCodes codes = extractNotificationCodes(request.getDebtPositionTypeOrgBalanceCost());

    CtCapitoloDefault capitoloNotifica = new CtCapitoloDefault();
    capitoloNotifica.setCodCapitolo(codes.sectionCode());
    capitoloNotifica.setCodUfficio(codes.officeCode());

    CtAccertamentoDefault accertamentoNotifica = new CtAccertamentoDefault();
    accertamentoNotifica.setCodAccertamento(codes.assessmentCode());

    BigDecimal notificationAmount = Utilities.longCentsToBigDecimalEuro(notificationFeeCents);
    accertamentoNotifica.setImporto(Utilities.amountToString(notificationAmount));

    capitoloNotifica.getAccertamentos().add(accertamentoNotifica);
    ctBilancioDefault.getCapitolos().add(capitoloNotifica);
  }

  private void addNotificationCostToBalance(Bilancio ctBilancio, CalculateAmountBalanceRequest request, Long notificationFeeCents) {
    log.info("Adding notification costs to the balance");
    NotificationCodes codes = extractNotificationCodes(request.getDebtPositionTypeOrgBalanceCost());

    CtCapitolo capitoloNotifica = new CtCapitolo();
    capitoloNotifica.setCodCapitolo(codes.sectionCode());
    capitoloNotifica.setCodUfficio(codes.officeCode());

    CtAccertamento accertamentoNotifica = new CtAccertamento();
    accertamentoNotifica.setCodAccertamento(codes.assessmentCode());

    BigDecimal notificationAmount = Utilities.longCentsToBigDecimalEuro(notificationFeeCents);
    accertamentoNotifica.setImporto(notificationAmount);

    capitoloNotifica.getAccertamentos().add(accertamentoNotifica);
    ctBilancio.getCapitolos().add(capitoloNotifica);
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

    return new NotificationCodes(DEFAULT_SEND_DPTOBC_CODE, DEFAULT_SEND_DPTOBC_CODE, DEFAULT_SEND_DPTOBC_CODE);
  }
}

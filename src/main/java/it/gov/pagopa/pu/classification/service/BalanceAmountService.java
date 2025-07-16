package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.dto.generated.CalculateAmountBalanceRequest;
import it.gov.pagopa.pu.classification.exception.custom.InvalidValueException;
import it.gov.pagopa.pu.classification.util.Utilities;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtBilancio;
import it.veneto.regione.schemas._2012.pagamenti.ente.bilanciodefault.CtAccertamentoDefault;
import it.veneto.regione.schemas._2012.pagamenti.ente.bilanciodefault.CtBilancioDefault;
import it.veneto.regione.schemas._2012.pagamenti.ente.bilanciodefault.CtCapitoloDefault;
import org.springframework.stereotype.Service;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.math.BigDecimal;

@Service
public class BalanceAmountService {

  public static final String BILANCIO_DEFAULT_TOTALE = "TOTALE";
  public static final String BILANCIO_DEFAULT_ESTRAI_IMPORTO = "estrai_importo";
  public static final String BILANCIO_DEFAULT_CALCOLA_IMPORTO = "calcola_importo";

  private final BalanceService balanceService;
  private final BalanceDefaultMarshallingService balanceDefaultMarshallingService;
  private final ScriptEngineManager factory = new ScriptEngineManager();
  private final ScriptEngine engine = factory.getEngineByName("rhino");

  public BalanceAmountService(BalanceService balanceService, BalanceDefaultMarshallingService balanceDefaultMarshallingService) {
    this.balanceService = balanceService;
    this.balanceDefaultMarshallingService = balanceDefaultMarshallingService;
  }

  public String calculateAmountBalance(CalculateAmountBalanceRequest calculateAmountBalanceRequest) {
    String balance = calculateAmountBalanceRequest.getBalance();
    BigDecimal amountInstallment = Utilities.longCentsToBigDecimalEuro(calculateAmountBalanceRequest.getAmountCents());

    Object balanceXML = balanceService.unmarshalBalance(balance);

    if (balanceXML instanceof CtBilancio) {
      return balance;
    }

    try {
      CtBilancioDefault ctBilancioDefault = (CtBilancioDefault) balanceXML;
      for (CtCapitoloDefault capitolo : ctBilancioDefault.getCapitolo()) {
        for (CtAccertamentoDefault ctAccertamentoDefault : capitolo.getAccertamento()) {
          BigDecimal calculatedAmount;
          if (ctAccertamentoDefault.getImporto().equals(BILANCIO_DEFAULT_TOTALE)) {
            calculatedAmount = amountInstallment;
          } else if (ctAccertamentoDefault.getImporto().contains(BILANCIO_DEFAULT_ESTRAI_IMPORTO)) {
            engine.eval(ctAccertamentoDefault.getImporto());
            Invocable invocable = (Invocable) engine;
            String result = String.valueOf(invocable.invokeFunction(BILANCIO_DEFAULT_ESTRAI_IMPORTO, calculateAmountBalanceRequest.getRemittanceInformation()));
            double resultDouble = Double.parseDouble(result);
            calculatedAmount = BigDecimal.valueOf(resultDouble);
          } else if (ctAccertamentoDefault.getImporto().contains(BILANCIO_DEFAULT_CALCOLA_IMPORTO)) {
            engine.eval(ctAccertamentoDefault.getImporto());
            Invocable invocable = (Invocable) engine;
            String result = String.valueOf(
              invocable.invokeFunction(BILANCIO_DEFAULT_CALCOLA_IMPORTO, amountInstallment));
            double resultDouble = Double.parseDouble(result);
            calculatedAmount = BigDecimal.valueOf(resultDouble);
          } else {
            throw new InvalidValueException("Function type to calculate amount balance not supported: " + ctAccertamentoDefault.getImporto());
          }
          String amountString = Utilities.amountToString(calculatedAmount);
          String amountWithoutSeparator = amountString.replace("\\.", "");
          String amountWithSeparatorAndDecimal = amountWithoutSeparator.replace(",", ".");

          ctAccertamentoDefault.setImporto(amountWithSeparatorAndDecimal);
        }
      }
      return balanceDefaultMarshallingService.marshal(ctBilancioDefault);
    } catch (Exception e) {
      throw new InvalidValueException("Error calculating amount of balance: " + e.getMessage());
    }
  }

}

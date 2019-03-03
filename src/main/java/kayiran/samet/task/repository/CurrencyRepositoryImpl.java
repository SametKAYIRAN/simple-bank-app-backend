package kayiran.samet.task.repository;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kayiran.samet.task.dto.CurrencyDto;

@Repository
public class CurrencyRepositoryImpl implements CurrencyRepository  {

	@Override
	public CurrencyDto getCurrencies() {
		
		CurrencyDto currencyDto = new CurrencyDto();

		try {
			String jsonCurrencies = Jsoup.connect("https://api.exchangeratesapi.io/latest?base=TRY")
					.ignoreContentType(true).execute().body();

			ObjectMapper mapper = new ObjectMapper();
			JsonNode actualObj = mapper.readTree(jsonCurrencies);
			JsonNode rates = actualObj.get("rates");

			float usd = Float.parseFloat(rates.get("USD").toString());
			float jpy = Float.parseFloat(rates.get("JPY").toString());
			float eur = Float.parseFloat(rates.get("EUR").toString());

			currencyDto.setEur(eur);
			currencyDto.setJpy(jpy);
			currencyDto.setUsd(usd);

		} catch (Exception e) {

			
			throw new InternalError("Internal server error.");
		}
		
		return currencyDto;
	}

}

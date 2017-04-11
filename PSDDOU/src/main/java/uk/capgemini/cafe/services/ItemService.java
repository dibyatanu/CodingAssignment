package uk.capgemini.cafe.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * Service class for adding Item to be purchased
 * 
 * @author Dibyatanu Deb
 *
 */
@Service
public class ItemService {
	private List<String> listOfItem = new ArrayList<>();
	private static Map<String, BigDecimal> listOfMenu = new HashMap<>();
	private static final String BILL_SUB_TOTAL = "Sub-Total";
	private static final String BILL_TOTAL = "Total";
	private static final String BILL_SERVICE_CHARGE_20 = "Service Charge @20";
	private static final String BILL_SERVICE_CHARGE_10 = "Service Charge @10";

	static {
		listOfMenu.put("Cola-Cold Drink", BigDecimal.valueOf(0.50));
		listOfMenu.put("Coffe-Hot Drink", BigDecimal.valueOf(1.00));
		listOfMenu.put("Cheese Sandwich-Cold Food", BigDecimal.valueOf(2.00));
		listOfMenu.put("Steak Sandwich-Hot Food", BigDecimal.valueOf(4.50));

	}

	public void addItem(final String[] listOfItem) {
		this.listOfItem = Arrays.asList(listOfItem);
	}

	public String[] getAddedItem() {
		return this.listOfItem.toArray(new String[0]);
	}

	public Map<String, BigDecimal> getMenu() {
		return listOfMenu;
	}

	public Map<String, BigDecimal> generateBill() {
		Map<String, BigDecimal> expectedBill = calculateSubTotalBill();
		expectedBill = calculateTotalBill(expectedBill);
		return expectedBill;
	}

	private Map<String, BigDecimal> calculateSubTotalBill() {
		Map<String, BigDecimal> expectedBill = new LinkedHashMap<>();
		BigDecimal subTotalCost = BigDecimal.ZERO;
		for (String item : listOfItem) {
			if (listOfMenu.containsKey(item)) {
				BigDecimal cost = listOfMenu.get(item);
				expectedBill.put(item, cost);
				subTotalCost = subTotalCost.add(cost);
			}
		}
		expectedBill.put(BILL_SUB_TOTAL, subTotalCost);
		return expectedBill;
	}

	private Map<String, BigDecimal> calculateTotalBill(final Map<String, BigDecimal> expectedBill) {
		// Calculate Service charge
		BigDecimal subTotalCost = expectedBill.get(BILL_SUB_TOTAL);
		BigDecimal serviceCharge;
		String serviceChargeKey;
		Boolean containsHotFood = containsHotFood();
		if (containsHotFood) {
			serviceCharge = calculateServiceChargeForHotFood(subTotalCost);
			serviceChargeKey=BILL_SERVICE_CHARGE_20;
		} else {
			serviceCharge = calculateServiceChargeColdFood(subTotalCost);
			serviceChargeKey=BILL_SERVICE_CHARGE_10;
		}
		expectedBill.put(serviceChargeKey, serviceCharge);
		BigDecimal totalCost = subTotalCost.add(serviceCharge);
		expectedBill.put(BILL_TOTAL, totalCost);
		return expectedBill;
	}

	private BigDecimal calculateServiceChargeForHotFood(final BigDecimal subTotalCost) {
		BigDecimal serviceCharge = subTotalCost.multiply(new BigDecimal(20)).divide(new BigDecimal(100));
		serviceCharge = serviceCharge.setScale(2, RoundingMode.CEILING);
		if (serviceCharge.compareTo(new BigDecimal(20)) > 0) {
			serviceCharge = new BigDecimal(20);
		}
		return serviceCharge;
	}

	private BigDecimal calculateServiceChargeColdFood(final BigDecimal subTotalCost) {
		BigDecimal serviceCharge = subTotalCost.multiply(new BigDecimal(10)).divide(new BigDecimal(100));
		serviceCharge = serviceCharge.setScale(2, RoundingMode.CEILING);
		return serviceCharge;
	}

	private Boolean containsHotFood() {
		Boolean containHotFood = Boolean.FALSE;

		for (String item : listOfItem) {
			containHotFood = item.contains("Hot Food");
		}

		return containHotFood;
	}
}

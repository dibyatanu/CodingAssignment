package uk.capgemini.cafe.services;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.capgemini.cafe.CafeApplicationStarter;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=CafeApplicationStarter.class)
public class ItemServiceTest {
	@Autowired
	public  ItemService itemService;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void getMenu_ShouldReturnListOfAvilableMenu() {
		/*arrange*/
		Map<String,BigDecimal> expectedMenuList=getExpectedMenuList();
		/*act*/
		Map<String,BigDecimal> actualMenuList=itemService.getMenu();
		/*assert*/
		Assert.assertEquals(expectedMenuList, actualMenuList);

	}
	@Test
	public void addItem_ShouldAddSelectedItemToBePurchased()
	{
		/*arrange*/
		String[]itemsTobePurchased= getListOfItemToBePurchased();

		/*act*/
		itemService.addItem(itemsTobePurchased);

		/*assert*/
		String[] actualItemPruchase=itemService.getAddedItem();
		Assert.assertArrayEquals(itemsTobePurchased, actualItemPruchase);
	}
	/*git commit -m "Story 1" */

	@Test
	public void generateBill_ShouldGenerateBillForTheItemsPurchased()
	{
		/*arrange*/
		String[]itemsTobePurchased= getListOfItemToBePurchased();
		Map<String,BigDecimal> expectedBill=calculateExpectedBill(itemsTobePurchased);
		itemService.addItem(itemsTobePurchased);

		/*act*/
		Map<String,BigDecimal> actualBill=itemService.generateBill();

		/*assert*/
		assertThat(actualBill, equalTo(expectedBill));
	}
	/*git commit -m "Story 2-Standard Bill" */
	/*git commit -m "Story 4-Service Charge" */
	
	@Test
	public void generateBill_ShouldGenerateBillForTheItemsPurchasedContaingHotFood()
	{
		/*arrange*/
		String[]itemsTobePurchased= getListOfItemToBePurchasedContainingHotFood();
		Map<String,BigDecimal> expectedBill=calculateExpectedBill(itemsTobePurchased);
		itemService.addItem(itemsTobePurchased);

		/*act*/
		Map<String,BigDecimal> actualBill=itemService.generateBill();

		/*assert*/
		assertThat(actualBill, equalTo(expectedBill));
	}

	/*git commit -m "Story 5-Service Charge" */
	@Test
	public void generateBill_ShouldGenerateServiceChargeRoundedToTwoDecimalPoint()
	{
		/*arrange*/
		String[]itemsTobePurchased= getListOfItemToBePurchasedContainingHotFood();
		Map<String,BigDecimal> expectedBill=calculateExpectedBill(itemsTobePurchased);
		itemService.addItem(itemsTobePurchased);

		/*act*/
		Map<String,BigDecimal> actualBill=itemService.generateBill();

		/*assert*/
		assertThat(actualBill, equalTo(expectedBill));
	}
	/*git commit -m "Story 6-Service Charge-Rounded to two decimal point" */
	
	@Test
	public void generateBill_ShouldGenerateServiceChargeMaxTwenty()
	{
		/*arrange*/
		String[]itemsTobePurchased= getListOfItemToBePurchasedContainingHotFood();
		itemService.addItem(itemsTobePurchased);

		/*act*/
		Map<String,BigDecimal> actualBill=itemService.generateBill();

		/*assert*/
		assertThat(	actualBill,hasKey("Service Charge @20"));
		BigDecimal actualServiceCharge=actualBill.get("Service Charge @20");
		assertThat(	actualServiceCharge,lessThanOrEqualTo(new BigDecimal(20)));
	}
	/*git commit -m "Story 7-Service Charge max of £20 on hot food" */

	@Test
	public void generateBill_ShouldGenerateItemizedBill()
	{
		/*arrange*/
		String[]itemsTobePurchased= getListOfItemToBePurchasedContainingHotFood();
		Map<String,BigDecimal> expectedBill=calculateExpectedBill(itemsTobePurchased);
		itemService.addItem(itemsTobePurchased);

		/*act*/
		Map<String,BigDecimal> actualBill=itemService.generateBill();

		/*assert*/
		assertThat(actualBill, equalTo(expectedBill));
	}
	/*git commit -m "Story 8-Generate Itmeized Bill" */
	
	
	private Map<String,BigDecimal> getExpectedMenuList()
	{
		Map<String,BigDecimal> listOfMenu= new HashMap<>();
		listOfMenu.put("Cola-Cold Drink",  BigDecimal.valueOf(0.50));
		listOfMenu.put("Coffe-Hot Drink", BigDecimal.valueOf(1.00));
		listOfMenu.put("Cheese Sandwich-Cold Food",  BigDecimal.valueOf(2.00));
		listOfMenu.put("Steak Sandwich-Hot Food",  BigDecimal.valueOf(4.50));
		return listOfMenu;
	}
	
	


	private String[] getListOfItemToBePurchased()
	{   String [] listOfItemToBePurchased={"Cola-Cold Drink","Cheese Sandwich-Cold Food"};
	return listOfItemToBePurchased;
	}
	
	private String[] getListOfItemToBePurchasedContainingHotFood()
	{   String [] listOfItemToBePurchased={"Cola-Cold Drink","Steak Sandwich-Hot Food"};
	return listOfItemToBePurchased;
	}

	private Map<String,BigDecimal> calculateExpectedBill(String[] itemsTobePurchased)
	{
		Map<String,BigDecimal> expectedBill= new LinkedHashMap<>();
		Map<String,BigDecimal> menuList=getExpectedMenuList();
		BigDecimal subTotalCost=BigDecimal.ZERO ;
		for(String item:itemsTobePurchased)
		{
			if(menuList.containsKey(item))
			{
				BigDecimal cost=menuList.get(item);
				expectedBill.put(item, cost);
				subTotalCost=subTotalCost.add(cost);
			};
		}
		expectedBill.put("Sub-Total", subTotalCost);
		
		BigDecimal serviceCharge=BigDecimal.ZERO;
		if(containsHotFood(itemsTobePurchased))
		{
			serviceCharge = subTotalCost.multiply(new BigDecimal(20)).divide(new BigDecimal(100));
			serviceCharge=serviceCharge.setScale(2, RoundingMode.CEILING);
			expectedBill.put("Service Charge @20", serviceCharge);
		}else
		{
			serviceCharge = subTotalCost.multiply(new BigDecimal(10)).divide(new BigDecimal(100));
			serviceCharge=serviceCharge.setScale(2, RoundingMode.CEILING);
			expectedBill.put("Service Charge @10", serviceCharge);
		}
		
		BigDecimal totalCost=subTotalCost.add(serviceCharge);		

		expectedBill.put("Total", totalCost);
		return expectedBill;
	}

	private Boolean containsHotFood(String[] itemsTobePurchased)
	{   Boolean containHotFood=Boolean.FALSE;

	for(String item:itemsTobePurchased)
	{
		containHotFood= item.contains("Hot Food");
	}

	return containHotFood;
	}

}

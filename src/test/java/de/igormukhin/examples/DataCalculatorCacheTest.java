package de.igormukhin.examples;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class DataCalculatorCacheTest {

	@Autowired DataCalculator calculator;
	
	@Autowired CacheManager cacheManager;
	
	@Before
	public void before() {
		// clear the ehCache explicitly because even @DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
		// doesn't do it. EhCache somehow keeps the results between separate Spring Contexts. 
		((EhCacheCacheManager) cacheManager).getCacheManager().clearAll();
	}
	
	@Test
	public void testCalculation() {
		assertThat(calculator.calculate(1), equalTo(1001));
		assertThat(calculator.calculate(2), equalTo(1002));
		assertThat(calculator.calculate(50), equalTo(1050));
		assertThat(calculator.getCalculations(), equalTo(3));
	}
	
	@Test
	public void testCaching() {
		assertThat(calculator.calculate(1), equalTo(1001));
		assertThat(calculator.getCalculations(), equalTo(1));

		assertThat(calculator.calculate(2), equalTo(1002));
		assertThat(calculator.getCalculations(), equalTo(2));

		assertThat(calculator.calculate(1), equalTo(1001));
		assertThat(calculator.getCalculations(), equalTo(2));
	}
	
	@Test
	public void testRecalcAfterEviction() {
		assertThat(calculator.calculate(1), equalTo(1001));
		assertThat(calculator.getCalculations(), equalTo(1));

		calculator.reset(1);

		assertThat(calculator.calculate(1), equalTo(1001));
		assertThat(calculator.getCalculations(), equalTo(2));
	}
	
	@Test
	public void testNoRecalcAfterAnotherEviction() {
		assertThat(calculator.calculate(1), equalTo(1001));
		assertThat(calculator.getCalculations(), equalTo(1));

		calculator.reset(2);

		assertThat(calculator.calculate(1), equalTo(1001));
		assertThat(calculator.getCalculations(), equalTo(1));
	}
}

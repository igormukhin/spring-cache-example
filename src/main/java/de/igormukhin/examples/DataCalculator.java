package de.igormukhin.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DataCalculator {
	private static final Logger logger = LoggerFactory.getLogger(DataCalculator.class);
	
	static final int CALC_SHIFT = 1000;
	
	private int calculations = 0;

	@Cacheable(value = "dataCache", key = "#id")
	public int calculate(int id) {
		calculations++;
		logger.info("calculate called for the {}th time", calculations);
		return id + CALC_SHIFT;
	}
	
	@CacheEvict(value = "dataCache", key = "#id")
	public void reset(int id) {
		// no-op
	}

	public int getCalculations() {
		return calculations;
	}
	
}

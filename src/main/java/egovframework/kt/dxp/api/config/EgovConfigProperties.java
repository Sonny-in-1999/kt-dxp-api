package egovframework.kt.dxp.api.config;

import org.egovframe.rte.fdl.property.impl.EgovPropertyServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

//@Configuration
@Deprecated
public class EgovConfigProperties {

	@Bean(destroyMethod="destroy")
	public EgovPropertyServiceImpl propertiesService() {
		Map<String, String> properties = new HashMap<>();
		properties.put("pageUnit", "10");
		properties.put("pageSize", "10");

		EgovPropertyServiceImpl egovPropertyServiceImpl = new EgovPropertyServiceImpl();
		egovPropertyServiceImpl.setProperties(properties);
		return egovPropertyServiceImpl;
	}

}

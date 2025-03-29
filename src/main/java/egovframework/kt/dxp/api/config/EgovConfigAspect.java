package egovframework.kt.dxp.api.config;

import org.egovframe.rte.fdl.cmmn.aspect.ExceptionTransfer;
import org.egovframe.rte.fdl.cmmn.exception.handler.ExceptionHandler;
import org.egovframe.rte.fdl.cmmn.exception.manager.DefaultExceptionHandleManager;
import org.egovframe.rte.fdl.cmmn.exception.manager.ExceptionHandlerService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.util.AntPathMatcher;

import egovframework.kt.dxp.api.common.exception.EgovAopExceptionTransfer;
import egovframework.kt.dxp.api.common.exception.EgovSampleExcepHndlr;
import egovframework.kt.dxp.api.common.exception.EgovSampleOthersExcepHndlr;

//@Configuration
//@EnableAspectJAutoProxy
@Deprecated
public class EgovConfigAspect {

	@Bean
	public EgovSampleExcepHndlr egovHandler() {
		return new EgovSampleExcepHndlr();
	}

	@Bean
	public EgovSampleOthersExcepHndlr otherHandler() {
		return new EgovSampleOthersExcepHndlr();
	}

	@Bean
	public DefaultExceptionHandleManager defaultExceptionHandleManager(AntPathMatcher antPathMatcher, EgovSampleExcepHndlr egovHandler) {
		DefaultExceptionHandleManager defaultExceptionHandleManager = new DefaultExceptionHandleManager();
		defaultExceptionHandleManager.setReqExpMatcher(antPathMatcher);
		defaultExceptionHandleManager.setPatterns(new String[]{"**Service*Impl.*"});
		defaultExceptionHandleManager.setHandlers(new ExceptionHandler[]{egovHandler});
		return defaultExceptionHandleManager;
	}

	@Bean
	public DefaultExceptionHandleManager otherExceptionHandleManager(AntPathMatcher antPathMatcher, EgovSampleOthersExcepHndlr othersExcepHndlr) {
		DefaultExceptionHandleManager defaultExceptionHandleManager = new DefaultExceptionHandleManager();
		defaultExceptionHandleManager.setReqExpMatcher(antPathMatcher);
		defaultExceptionHandleManager.setPatterns(new String[]{"**Service*Impl.*"});
		defaultExceptionHandleManager.setHandlers(new ExceptionHandler[]{othersExcepHndlr});
		return defaultExceptionHandleManager;
	}

	@Bean
	public ExceptionTransfer exceptionTransfer(
		@Qualifier("defaultExceptionHandleManager") DefaultExceptionHandleManager defaultExceptionHandleManager,
		@Qualifier("otherExceptionHandleManager") DefaultExceptionHandleManager otherExceptionHandleManager) {
		ExceptionTransfer exceptionTransfer = new ExceptionTransfer();
		exceptionTransfer.setExceptionHandlerService(new ExceptionHandlerService[] {
			defaultExceptionHandleManager, otherExceptionHandleManager
		});
		return exceptionTransfer;
	}

	@Bean
	public EgovAopExceptionTransfer aopExceptionTransfer(ExceptionTransfer exceptionTransfer) {
		EgovAopExceptionTransfer egovAopExceptionTransfer = new EgovAopExceptionTransfer();
		egovAopExceptionTransfer.setExceptionTransfer(exceptionTransfer);
		return egovAopExceptionTransfer;
	}

}

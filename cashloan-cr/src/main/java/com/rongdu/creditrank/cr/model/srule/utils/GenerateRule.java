package com.rongdu.creditrank.cr.model.srule.utils;

import org.apache.log4j.Logger;

import tool.util.StringUtil;

import com.rongdu.creditrank.cr.model.srule.config.builder.RuleBuilder;
import com.rongdu.creditrank.cr.model.srule.config.builder.RuleBuilderCreator;
import com.rongdu.creditrank.cr.model.srule.config.condition.ConditionItem;
import com.rongdu.creditrank.cr.model.srule.config.rule.Rule;
import com.rongdu.creditrank.cr.model.srule.model.Formula;
import com.rongdu.creditrank.cr.model.srule.model.SimpleRule;

/**
 * 规则校验
 * @author ctt
 * @version 1.0.0
 * @date 2016年12月22日 下午3:45:26
 * Copyright 杭州融都科技股份有限公司 creditrank  All Rights Reserved
 * 官方网站：www.erongdu.com
 * 
 * 未经授权不得进行修改、复制、出售及商业使用
 */
public class GenerateRule {
	
	private static final Logger logger = Logger.getLogger(GenerateRule.class);
	
    /**
     * 字符类型的对比规则
     * @param index 规则序号
     * @param colName 列名
     * @param rule 对比规则
     * @param scop 对比范围约束
     * @param value 需要做对比的值
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws Exception
     */
    public static boolean genTextResult(String rule,String scop,String value) {
    	return comparText(rule,scop,value);
    }
    
    public static boolean comparText(String formula,String scop,String value){
    	boolean result = false;
    	if(Formula.include.getName().equals(formula)){
    		result = StringUtil.contains(scop, value);
    	}else if(Formula.exclude.getName().equals(formula)){
    		result = !StringUtil.contains(scop, value);
    	}else if(Formula.equal.getName().equals(formula)){
        		result = StringUtil.equals(scop, value);
        }else if(Formula.not_equal.getName().equals(formula)){
        		result = !StringUtil.equals(scop, value);
    	}else{	
    		//如果要判断的这的值为数字类型，可比较值大小
    	/*	if(StringUtil.isNumber(scop)&&StringUtil.isNumber(scop)){
    			//生成数字类型的规则
    			SimpleRule simpleRule = new SimpleRule((long) 0, "name", formula, value, scop, "int", "");
    			Rule rule = GenerateRule.genNumRule(simpleRule);
    			result = rule.beginMatch();	
    		}*/
    		//没有匹配的表达式
    	}
    	return result;
    }
    
    /**
     * 数字类型的对比规则
     * @param index 规则序号
     * @param colName 列名
     * @param rule 对比规则
     * @param scop 对比范围约束
     * @param value 需要做对比的值
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws Exception
     */
    public static Rule genNumRule(Long id,String colName,String rule,String scop,Double value){
    	RuleBuilder<Double> builder = RuleBuilderCreator.numRuleBuilder();
    	ConditionItem items = builder.newConditionItems();
		items.add(rule, Double.valueOf(scop));
		Rule rtRule = null;
		try {
			rtRule = builder.newRule(id, colName, items).rulePolicy(RulePolicy.MATCHALL).build();
			rtRule.matchTo(value);
		} catch (IllegalAccessException e) {
			logger.error(e);
		} catch (InstantiationException e) {
			logger.error(e);
		} catch (Exception e) {
			logger.error(e);
		}
		
		return rtRule;
    }
    
    /**
     * 获得字符类型的匹配结果
     * @param builder
     * @param formula
     * @param range
     * @return
     */
    public static boolean genTextResult(SimpleRule rule){
    	return comparText(rule.getFormula(),rule.getRange(),rule.getValue());
    }
    
    /**
     * 数字类型的对比规则
     * @param index 规则序号
     * @param colName 列名
     * @param rule 对比规则
     * @param scop 对比范围约束
     * @param value 需要做对比的值
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws Exception
     */
    public static Rule genNumRule(SimpleRule rule){
    	
    	RuleBuilder<Double> builder = RuleBuilderCreator.numRuleBuilder();
    	ConditionItem items = buildNumItems(builder, rule.getFormula(), rule.getRange());
		Rule rtRule = null;
		try {
			rtRule = builder.newRule(rule.getRuleId(), rule.getName(), items).rulePolicy(RulePolicy.MATCHALL).build();
			rtRule.matchTo(Double.valueOf(rule.getValue()));
		} catch (IllegalAccessException e) {
			logger.error(e);
		} catch (InstantiationException e) {
			logger.error(e);
		} catch (Exception e) {
			logger.error(e);
		}
		
		return rtRule;
    }
    
    /**
     * 组装数字类型的匹配表达式
     * @param builder
     * @param formula
     * @param range
     * @return
     */
    public static ConditionItem buildNumItems(RuleBuilder<Double> builder,String formula,String range){
    	ConditionItem items = builder.newConditionItems();
    	if(Formula.greater.getName().equals(formula)){
    		items.add(Formula.greater.getName(), Double.valueOf(range));
    	}else if(Formula.less.getName().equals(formula)){
    		items.add(Formula.less.getName(), Double.valueOf(range));
    	}else if(Formula.equal.getName().equals(formula)){
    		items.add(Formula.equal.getName(), Double.valueOf(range));
    	}else if(Formula.not_equal.getName().equals(formula)){
    		items.add(Formula.not_equal.getName(), Double.valueOf(range));
    	}else if(Formula.greater_equal.getName().equals(formula)){
    		items.add(Formula.greater_equal.getName(), Double.valueOf(range));
    	}else if(Formula.less_equal.getName().equals(formula)){
    		items.add(Formula.less_equal.getName(), Double.valueOf(range));
    	}else if(Formula.greater_equal_and_less_equal.getName().equals(formula)){
    		String[] ranges = range.trim().split(",");
    		items.add(Formula.greater_equal.getName(), Double.valueOf(ranges[0]));
    		items.add(Formula.less_equal.getName(), Double.valueOf(ranges[1]));
    	}else if(Formula.greater_equal_and_less.getName().equals(formula)){
    		String[] ranges = range.trim().split(",");
    		items.add(Formula.greater_equal.getName(), Double.valueOf(ranges[0]));
    		items.add(Formula.less.getName(), Double.valueOf(ranges[1]));
    	}else if(Formula.greater_and_less_equal.getName().equals(formula)){
    		String[] ranges = range.trim().split(",");
    		items.add(Formula.greater.getName(), Double.valueOf(ranges[0]));
    		items.add(Formula.less_equal.getName(), Double.valueOf(ranges[1]));
    	}else if(Formula.greater_and_less.getName().equals(formula)){
    		String[] ranges = range.trim().split(",");
    		items.add(Formula.greater.getName(), Double.valueOf(ranges[0]));
    		items.add(Formula.less.getName(), Double.valueOf(ranges[1]));
    	}else{
    		//没有匹配的表达式
    	}
    	
    	return items;
    }
    
    
}

package com.xiji.cashloan.rule.model.srule.config.rule;

import java.util.List;
import java.util.Map;

import com.xiji.cashloan.rule.model.srule.config.condition.Condition;
import com.xiji.cashloan.rule.model.srule.exception.RuleValueException;
import com.xiji.cashloan.rule.model.srule.utils.RulePolicy;

/**
 * @author wnb
 * @version 1.0.0
 * @date 2018/11/27
 */
public abstract class AbstractRule<T> implements Rule, RuleBasic<T> {


    protected List<Condition<T>> conditions;

    protected RulePolicy policy = RulePolicy.MATCHALL;


    protected Map<T, Integer> preLoad;

    protected Class<T> valueClass;

    protected long id;

    protected String column;

    protected String name;

    protected T matchTo;


    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public void setColumn(String column) {
        this.column = column;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setValueType(Class<T> clazz) {
        this.valueClass = clazz;
    }

    @Override
    @SuppressWarnings("All")
    public void matchTo(Object o) throws RuleValueException {
        if (o instanceof Integer || o instanceof Long || o instanceof Float) {
            o = Double.valueOf(o.toString());
        }
        if (o.getClass() != valueClass) {
            throw new RuleValueException("the matchTo Object: " + o + " which type is " + o.getClass().getName() + ",is not fit for the " + valueClass.getName());
        }
        this.matchTo = (T) o;
    }


    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public String getColumn() {
        return this.column;
    }

    @Override
    public String getName() {
        return this.name;
    }


    @Override
    public void setConditions(List<Condition<T>> conditions) {
        this.conditions = conditions;
    }

    @Override
    public void setRulePolicy(RulePolicy rulePolicy) {
        this.policy = rulePolicy;
    }

    @Override
    public void setPreLoad(Map<T, Integer> preLoad) {
        this.preLoad = preLoad;
    }
}

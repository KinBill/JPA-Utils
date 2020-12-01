package com.haimacy.jpautil.support.expression;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

public interface ISelector {
    enum Operator {
        PICK, SUM, MAX, MIN, COUNT
    }

    Selection<?> getSelection(Root<?> root, CriteriaBuilder builder);

    String getAlis();

}

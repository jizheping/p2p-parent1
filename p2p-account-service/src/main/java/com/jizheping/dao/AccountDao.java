package com.jizheping.dao;

import com.jizheping.api.entity.Account;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 持久层整合Jpa，使用jpa自带方法
 */

@Repository
public interface AccountDao extends JpaRepository<Account,Long> {
}

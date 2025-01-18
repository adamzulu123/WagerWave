package com.ww.WagerWave.Repository;


import com.ww.WagerWave.Model.Coupon;
import com.ww.WagerWave.Model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon,Integer> {
    List<Coupon> findAllByUser(MyUser user);
}

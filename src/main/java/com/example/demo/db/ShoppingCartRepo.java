package com.example.demo.db;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.beans.Coupon;
import com.example.demo.beans.ShoppingCart;

public interface ShoppingCartRepo extends JpaRepository<ShoppingCart, String> {
	
	@Query(value="select * from coupons JOIN shopping_cart_coupon on coupon.id=shopping_cart_coupon.coupon_id where "
			+ "shopping_cart_id=:cartId", nativeQuery=true)
	public Set<Coupon>getCartItems(String cartId);

	
	@Query(value="select * from shopping_cart JOIN customers on shopping_cart.customer_id=customers.id where "
			+ "customer_id=:customerId",nativeQuery=true)
	 ShoppingCart getByCustomerId(int customerId);
		

	@Modifying
	@Transactional
	@Query(value="delete from shopping_cart where customer_id=:customerId",nativeQuery=true)
	 void  deleteCustomerCart(int customerId);
}
	
	
//	@Query(value="select * from coupons join customers_vs_coupons on coupons.id=customers_vs_coupons.coupon_ID where"
//			+ " Customer_ID=:customerID", nativeQuery=true)
//	List<Coupon>findByCustomerId(int customerID);//JOIN- another table


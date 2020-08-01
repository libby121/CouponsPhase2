package com.example.demo.db;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.beans.Category;
import com.example.demo.beans.Coupon;

/**
 * Repositories interfaces are used for accessing (and persisting) database
 * information. The three repositories extend JPARepository which enables basic
 * CRUD operations.
 * 
 * @author ליבי
 *
 */
public interface CouponRepository extends JpaRepository<Coupon, Integer> {

	List<Coupon> findByCompanyId(int id);

	@Query("select c from Coupon c where c.company.id=:companyId and c.category=:cat")
	List<Coupon> findCompanyCouponsByCatgory(int companyId, Category cat);

	List<Coupon> findByCompanyIdAndPriceLessThanEqual(int compnyID, double maxPrice);

	/**
	 * Requires JOIN operation since the customer_id is not referenced in the
	 * coupons table, it has to be taken from the @ManyToMany coupons purchases
	 * table.
	 * 
	 * @param customerID
	 * @return
	 */
	@Query(value = "select * from coupons join customers_vs_coupons on coupons.id=customers_vs_coupons.coupon_ID where"
			+ " Customer_ID=:customerID", nativeQuery = true)
	List<Coupon> findByCustomerId(int customerID);// JOIN- another table

	@Query(value = "select * from coupons join customers_vs_coupons on coupons.id=customers_vs_coupons.coupon_ID wher"
			+ "e Customer_ID=:customerID and Category=:#{#category.ordinal()}", nativeQuery = true)
	List<Coupon> findByCustomerIdAndCategory(int customerID, Category category);

	@Query(value = "select * from coupons join customers_vs_coupons on coupons.id=customers_vs_coupons.coupon_ID where"
			+ " Customer_ID=:customerID and price<=:max", nativeQuery = true)
	List<Coupon> findByCustomerIdAndPriceLessThanEqual(int customerID, double max);

	/**
	 * Coupon purchases history deletion. Will be needed in deleteCompany() and
	 * deleteCustomer() methods.
	 * 
	 * @param custId
	 * @param coupId
	 */
	@Modifying
	@Transactional
	@Query(value = "delete from customers_vs_coupons where customer_id=:custId and coupon_id=:coupId", nativeQuery = true)
	void deleteCouponPurchase(int custId, int coupId);

	@Modifying
	@Transactional
	@Query(value="delete from shopping_cart_coupons where coupons_id=:coupId",nativeQuery=true)
	void deleteCouponFromCart( int coupId);
	
	
	
	
	
	
	
	
	
	

}

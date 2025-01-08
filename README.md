# Dealify | صفقتنا

**"Dealify | صفقتنا"** is an innovative platform that offers users the opportunity to purchase products at discounted prices by gathering a sufficient number of participants for the same deal.

---

## Objective
- Enable users to enjoy reduced prices through collective purchasing.
- Provide a seamless and reliable shopping experience.
- Increase merchants' profits by consolidating large orders.

---

## Target Audience
- Individuals seeking significant deals and offers.
- Merchants looking to showcase and sell their products efficiently.

---

## User Flow

### Registered Users
- Track their deals.
- Create or join deals.
- Review products, merchants, or other users they have participated with in deals.
- Request to be removed from the blocklist.
- Submit product return requests with reasons.

### Merchants
- Add new products.
- Add or remove users from the blocklist (Blocklist Management).
- Approve or reject return requests (Return Management).
- Rate customers.

---

### صفقتنا | Dealify

"Dealify | صفقتنا" هي منصة مبتكرة تتيح للمستخدمين شراء المنتجات بأسعار مخفضة من خلال تجمع عدد كافٍ من المشاركين في نفس الصفقة.

---

## الأهداف
- تمكين المستخدمين من الحصول على خصومات كبيرة عبر الشراء الجماعي.
- توفير تجربة تسوق سلسة وموثوقة.
- زيادة أرباح التجار عبر تجميع طلبات كبيرة.

---

## الشريحة المستهدفة
- الأفراد الباحثين عن صفقات وعروض مميزة.
- التجار الراغبين في عرض وبيع منتجاتهم بفعالية.

---

## تدفق المستخدمين

### المستخدمون المسجلون
- متابعة صفقاتهم.
- إنشاء صفقة أو الانضمام إلى صفقات قائمة.
- تقييم المنتجات، البائعين، أو المستخدمين الذين شاركوا معهم في الصفقات.
- طلب إزالة من القائمة السوداء.
- تقديم طلبات إرجاع المنتجات مع ذكر الأسباب.

### التجار
- إضافة منتجات جديدة.
- إضافة أو إزالة المستخدمين من القائمة السوداء (إدارة القائمة السوداء).
- قبول أو رفض طلبات الإرجاع (إدارة الطلبات).
- تقييم العملاء.

---

#
# Links
- [Figma](https://www.figma.com/design/kUyt9oIMPtgUqbXLnBDkS3/Dealify?node-id=0-1&t=GhXfJIKfyUDjqQfR-1)
- [Presentation](https://www.canva.com/design/DAGbZqlKQ60/OAW5OSI2w2P_I_IUJoBvkQ/edit?utm_content=DAGbZqlKQ60&utm_campaign=designshare&utm_medium=link2&utm_source=sharebutton)
- [PostMan API Documentation](https://documenter.getpostman.com/view/39709967/2sAYJAdGzY)
- ![QR Code](https://github.com/user-attachments/assets/dd59b236-3b36-40fc-91c6-270ec574485a)


#
# Diagrams
UseCase Diagrams:
- ![UseCase](https://github.com/user-attachments/assets/12d485e2-0cf4-4181-8254-3eb917b8670d)


Class Diagram:
- ![Class Diagram](https://github.com/user-attachments/assets/6df6f3d8-4659-480f-b86a-6cc68798e1a8)

---

## My Work

### Models-In/OutDTO-repository-CRUD:
- Blacklist  
- BlackListPardonRequest  
- CustomerDeal  
- Deal  
- Favorite  
- Image  
- ProductReview  
- VendorReview  

### JUnit tests:
**ProductRepositoryTest:**
- findProductByIdTesting  
- findProductsByCategoryNameTesting  
- findByStockLessThanTesting  
- findByPriceBetweenTesting  
- findProductByInventory  

### Endpoints:
- `/create-a-deal/product-id/{product-id}`  
  `createADeal`  

- `/get-customer-open-deals`  
  `viewCustomerOpenedDeals`  

- `/get-customer-completed-deals`  
  `viewCustomerCompletedDeals`  

- `/join-a-deal/deal-id/{deal-id}`  
  `joinADeal`  

- `/leave-a-deal/customer-deal-id/{customer-deal-id}`  
  `leaveADeal`  

- `/get-customer-favorites`  
  `getCustomerFavorites`  

- `/update-quantity-to-buy/customer-deal-id/{customer-deal-id}`  
  `updateQuantityToBuy`  

- `/add-product-to-favorite/product-id/{product-id}`  
  `addProductToFavorite`  

- `/remove-product-from-favorite/product-id/{product-id}`  
  `removeProductFromFavorite`  

- `/get-customer-reviews/customer-id/{customer-id}`  
  `getCustomerReviews`  

- `/review-a-customer/reviewed-id/{reviewed-id}`  
  `reviewACustomer`  

- `/update-a-customer-review/review-id/{review-id}`  
  `updateACustomerReview`  

- `/get-product-reviews/product-id/{product-id}`  
  `viewProductReviews`  

- `/review-a-product/product-id/{product-id}`  
  `reviewAProduct`  

- `/update-a-product-review/product-review-id/{product-review-id}`  
  `updateAProductReview`  

- `/get-customer-pardon-requests`  
  `getCustomerBlackListPardonRequests`  

- `/request-a-pardon/vendor-id/{vendor-id}`  
  `requestAPardon`  

- `/update-a-pardon-request/pardon-request-id/{pardon-request-id}`  
  `updateAPardonRequest`  

- `/pay/deal-id/{deal-id}`  
  `pay`  

- `/pay-with-points/deal-id/{deal-id}`  
  `payWithPoints`  

- `/decline-to-pay/deal-id/{deal-id}`  
  `declineToPay`  

- `/get-product-open-deals/{product-id}`  
  `getProductOpenDeals`  

- `/get-vendor-open-deals/vendor-id/{vendor-id}`  
  `viewVendorOpenDeals`  

- `/get-vendor-black-list`  
  `getVendorBlackList`  

- `/add-customer-to-blacklist/customer-id/{customer-id}`  
  `addCustomerToBlacklist`  

- `/remove-customer-from-blacklist/{customer-id}`  
  `removeCustomerFromBlackList`  

- `/get-vendor-blacklist-pardon-request`  
  `getVendorBlackListPardonRequests`  

- `/approve-pardon-request/{pardon-request-id}`  
  `approvePardonRequest`  

- `/reject-pardon-request/{pardon-request-id}`  
  `rejectPardonRequest`  

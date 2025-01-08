# Dealify | صفقتنا
Dealify is an innovative platform that offers users the opportunity to purchase products at discounted prices by gathering a sufficient number of participants for the same deal
#
Key Features:
- For Customers:
1. Track Deals:
      - Monitor active deals and progress.
2. Create or Join Deals:
      - Initiate a new group deal or participate in an existing one.
3. Ratings and Reviews:
      - Rate products, sellers, or other users within the same deal.
      - Evaluate the reliability of users (e.g., flag users who frequently abandon deals).
3. Requests:
      - Submit requests to be removed from the blacklist.
      - Request product returns with a detailed reason.


- For Vendors:
1. Product Management:
     - Add and manage products on the platform.
2. Blacklist Management:
     - Add or remove users from the blacklist.
3. Return Requests:
     - Approve or reject product return requests.
4. Client Reviews:
    - Evaluate and rate clients based on their participation behavior.
  
Dealify connects users and vendors in a unified platform, creating a collaborative shopping experience where group purchasing unlocks exclusive discounts, fostering a thriving community of deal seekers and sellers.
#

# النسخة العربية:

"صفقتنا" هي منصة مبتكرة تتيح للمستخدمين شراء المنتجات بأسعار مخفضة من خلال جمع عدد محدد من المشاركين في نفس الصفقة.

الميزات الرئيسية:
- للعملاء:
1. متابعة الصفقات:
    - متابعة الصفقات النشطة وتقدمها.
2. إنشاء أو الانضمام إلى صفقات:
    - بدء صفقة جماعية جديدة أو المشاركة في صفقة موجودة.
3. التقييمات والمراجعات:
    - تقييم المنتجات أو البائعين أو المستخدمين الآخرين ضمن نفس الصفقة.
    - تقييم موثوقية المستخدمين (مثلًا، الإشارة إلى المستخدمين الذين يتخلفون عن إتمام الصفقات).
4. الطلبات:
    - تقديم طلبات لإزالة المستخدمين من القائمة السوداء.
    - طلب استرجاع المنتجات مع تقديم سبب مفصل.


- للبائعين:
1. إدارة المنتجات:
    - إضافة وإدارة المنتجات على المنصة.
2. إدارة القائمة السوداء:
    - إضافة أو إزالة المستخدمين من القائمة السوداء.
3. طلبات الإرجاع:
    - الموافقة أو الرفض على طلبات إرجاع المنتجات.
4. مراجعات العملاء:
    - تقييم العملاء بناءً على سلوكهم في المشاركة في الصفقات.
  
صفقتنا تربط المستخدمين والبائعين في منصة موحدة، مما يخلق تجربة تسوق تعاونية حيث تتيح الشراء الجماعي الحصول على خصومات حصرية، مما يعزز مجتمعًا مزدهرًا من الباحثين عن الصفقات والبائعين.

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

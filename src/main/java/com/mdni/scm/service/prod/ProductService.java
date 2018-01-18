package com.mdni.scm.service.prod;

import com.google.common.collect.Maps;
import com.mdni.scm.common.dto.BootstrapPage;
import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.entity.eum.ProductStatusEnum;
import com.mdni.scm.entity.eum.StatusEnum;
import com.mdni.scm.entity.prod.*;
import com.mdni.scm.repository.prod.ProductDao;
import com.mdni.scm.rest.prod.SkuRestController;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.Collections3;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <dl>
 * <dd>描述: 商品Service</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月15日 下午2:54:29</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@Service
public class ProductService extends CrudService<ProductDao, Product> {
	@Autowired
	private CatalogService catalogService;
	@Autowired
	private BrandService brandService;
	@Autowired
	private ProductImageService productImageService;
	@Autowired
	private SkuMetaService skuMetaService;
	@Autowired
	private SkuPriceService skuPriceService;
	@Autowired
	private SkuService skuService;
	@Autowired
	private SupplierService supplierService;
	@Autowired
	private RegionSupplierService regionSupplierService;
	@Autowired
	private StoreService storeService;

	public BootstrapPage<Product> adminSearch(Map<String, Object> searchParamMap) {
		List<Product> pageData = Collections.emptyList();
		Long count = this.entityDao.adminSearchTotal(searchParamMap);
		if (count > 0) {
			pageData = entityDao.adminSearch(searchParamMap);
		}
		return new BootstrapPage(count, pageData);
	}

	public List<String> findAllCode() {
		return this.entityDao.findAllCode();
	}

	/**
	 * 判断 商品代码是否已经存在
	 */
	public boolean isCodeExist(Product product) {
		if (product == null || StringUtils.isBlank(product.getCode())) {
			return false;
		}
		return this.entityDao.getByCode(product) != null;
	}

	/**
	 * 创建一个商品
	 * 
	 * @param product
	 */
	@Transactional(rollbackFor = Exception.class)
	public void create(Product product) {
		if (product == null) {
			return;
		}

		entityDao.insert(product);

		if (CollectionUtils.isNotEmpty(product.getProductImages())) {
			for (ProductImage image : product.getProductImages()) {
				image.setProductId(product.getId());
				productImageService.insert(image);
			}
		}

		if (product.getSkuMeta() != null) {
			product.getSkuMeta().setProductId(product.getId());
			skuMetaService.insert(product.getSkuMeta());
		}

		if (CollectionUtils.isNotEmpty(product.getSkus())) {
			for (int idx = 0; idx < product.getSkus().size(); idx++) {
				Sku sku = product.getSkus().get(idx);
				sku.setProduct(product);
				sku.setSupplierId(product.getSupplier().getId());
				sku.setProcessStatus(SkuRestController.SKU_DRAFT);
				skuService.insert(sku);
			}
		}
	}


	/**
	 * 创建一个商品设置价格 并且是上架的
	 *
	 * @param product
	 */
	@Transactional(rollbackFor = Exception.class)
	public void createSkuAndPrice(Product product) {
		if (product == null) {
			return;
		}
		entityDao.insert(product);
		if (CollectionUtils.isNotEmpty(product.getProductImages())) {
			for (ProductImage image : product.getProductImages()) {
				image.setProductId(product.getId());
				productImageService.insert(image);
			}
		}
		if (product.getSkuMeta() != null) {
			product.getSkuMeta().setProductId(product.getId());
			skuMetaService.insert(product.getSkuMeta());
		}
		if (CollectionUtils.isNotEmpty(product.getSkus())) {
			for (int idx = 0; idx < product.getSkus().size(); idx++) {
				Sku sku = product.getSkus().get(idx);
				sku.setProduct(product);
				sku.setSupplierId(product.getSupplier().getId());
				sku.setProcessStatus(SkuRestController.SKU_SHELF_SHELVES);
				skuService.insert(sku);
				if (CollectionUtils.isNotEmpty(sku.getPriceList())) {
					List<SkuPrice> priceList = sku.getPriceList();
					for (int i = 0; i <priceList.size(); i++) {
						SkuPrice skuPrice = priceList.get(i);
						skuPrice.setSkuId(sku.getId());
						skuPriceService.insert(skuPrice);
					}
				}
			}
		}
	}

	/**
	 * 获得商品详细信息:包括：商品基本信息、品主图、sku列表,sku元数据,sku图片、商品供应商
	 * 
	 * @param productId 商品id
	 */
	public Product getProductDetailById(Long productId) {
		Product product = this.getById(productId);
		if (product != null) {

			product.setSkuMeta(skuMetaService.getByProductId(productId));
			product.setSkus(skuService.findByProductId(productId));
			product.setProductImages(productImageService.findProductPrimaryImages(productId));

			if (product.getSupplier() != null) {
				product.setSupplier(supplierService.getById(product.getSupplier().getId()));

				if (product.getSupplier().getRegionSupplier() != null
					&& product.getSupplier().getRegionSupplier().getId() > 0) {

					RegionSupplier regionSupplier = regionSupplierService.getById(product.getSupplier()
						.getRegionSupplier().getId());
					product.getSupplier().setRegionSupplier(regionSupplier);

					if (regionSupplier != null && StringUtils.isNotBlank( regionSupplier.getStore().getCode() ) ) {
						Store store = storeService.getByCode(regionSupplier.getStore().getCode());
						regionSupplier.setStore(store);
					}
				}
			}

			if (CollectionUtils.isNotEmpty(product.getSkus())) {
				Map<Long, List<ProductImage>> skuImgListMap = productImageService.findSkuImgsListMap(productId);
				for (Sku sku : product.getSkus()) {
					sku.setProductImages(skuImgListMap.get(sku.getId()));
				}
			}
		}
		return product;
	}

	/**
	 * 更新商品 (后台专用)。
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(Product product) {
		if (product == null || product.getId() == null)
			return;

		entityDao.update(product);
		String name = product.getName();
		//修改商品名字
		if(!StringUtils.isEmpty(name)){
			List<Sku> byProductId = skuService.findByProductId(product.getId());
			for (Sku sku:byProductId) {
				String name1 = sku.getName();
				Sku sku1 = new Sku();
				sku1.setId(sku.getId());
				sku1.setName(name1.replace(name1.split(" ")[0].toString(),name));
				skuService.updateSku(sku1);
			}
		}

		//更新图片
		if (product.getProductImages() != null) {
			List<ProductImage> oldProductImages = productImageService.findProductPrimaryImages(product.getId());
			productImageService.updateProductMainImgs(oldProductImages, product.getProductImages(), product.getId());
		}

		if (product.getSkuMeta() != null) {
			product.getSkuMeta().setProductId(product.getId());
			skuMetaService.update(product.getSkuMeta());
		}

		if (product.getSkus() != null) {
			for (Sku sku : product.getSkus()) {
				sku.setProduct(product);
				if (sku.getId() == null) {
					sku.setSupplierId(sku.getProduct().getSupplier().getId());
					skuService.insert(sku);
				} else {
					skuService.update(sku);
				}
			}
		}
	}

	/**
	 * 通过id列表获得商品列表
	 * 
	 * @param productIdList 产品Id列表
	 * @param status 商品状态，可以为null,null则表示返回所有状态
	 */
	public List<Product> findByIdList(final List<Long> productIdList, final StatusEnum status) {
		if (Collections3.isEmpty(productIdList)) {
			return Collections.emptyList();
		}
		return entityDao.findByIdInList(productIdList, status);
	}

	/**
	 * 返回排序后的 商品位置列表,返回的结果按照position或分类序 升序排列
	 * 
	 * @param categoryUrl 分类url,可以为null,如果为null,则按照全局序排列，否则按照分类序排列
	 */
	public List<Product> findOrderedPositionList(final String categoryUrl) {
		return this.entityDao.findOrderedPositionList(categoryUrl);
	}

	/**
	 * 将商品list转换到已id为key的map里
	 * 
	 * @param productList
	 */
	public static Map<Long, Product> productList2IdKeyMap(List<Product> productList) {
		Map<Long, Product> productMap = Maps.newHashMap();
		if (Collections3.isEmpty(productList))
			return productMap;
		for (Product product : productList) {
			productMap.put(product.getId(), product);
		}
		return productMap;
	}
	/**
	 * flag 1 2 商品作废/批量下架
	 */
	@Transactional(rollbackFor = Exception.class)
	public void batchSku(Long productId, String flag) {
		Sku sku = new Sku();
		if(flag.equals("1")){
			//商品修改
			Product product = new Product();
			product.setId(productId);
			product.setStatus(ProductStatusEnum.DELIST);
			this.entityDao.update(product);
			//作废
			sku.setProcessStatus(SkuRestController.SKU_VOID);
		}else if(flag.equals("2")) {
			//下架
			sku.setProcessStatus(SkuRestController.SKU_SHELF_FAILURE);
		}
		Product product = new Product();
		product.setId(productId);
		sku.setProduct(product);
		skuService.batchSku(sku);
	}

	/**
	 * 根据map参数查询商品
	 * @param map
	 * productName	商品名称
	 * brandName	品牌名称
	 * supplierName	供应商名称
	 * @return
	 */
	public Product getByMap(Map<String,String> map){
		return  entityDao.getByMap(map);
	}
}

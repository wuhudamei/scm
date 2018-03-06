package cn.damei.scm.service.prod;

import cn.damei.scm.entity.prod.*;
import com.google.common.collect.Maps;
import cn.damei.scm.common.dto.BootstrapPage;
import cn.damei.scm.common.service.CrudService;
import cn.damei.scm.entity.eum.ProductStatusEnum;
import cn.damei.scm.entity.eum.StatusEnum;
import cn.damei.scm.repository.prod.ProductDao;
import cn.damei.scm.rest.prod.SkuRestController;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.Collections3;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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

	public boolean isCodeExist(Product product) {
		if (product == null || StringUtils.isBlank(product.getCode())) {
			return false;
		}
		return this.entityDao.getByCode(product) != null;
	}

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

	public List<Product> findByIdList(final List<Long> productIdList, final StatusEnum status) {
		if (Collections3.isEmpty(productIdList)) {
			return Collections.emptyList();
		}
		return entityDao.findByIdInList(productIdList, status);
	}

	public List<Product> findOrderedPositionList(final String categoryUrl) {
		return this.entityDao.findOrderedPositionList(categoryUrl);
	}

	public static Map<Long, Product> productList2IdKeyMap(List<Product> productList) {
		Map<Long, Product> productMap = Maps.newHashMap();
		if (Collections3.isEmpty(productList))
			return productMap;
		for (Product product : productList) {
			productMap.put(product.getId(), product);
		}
		return productMap;
	}
	@Transactional(rollbackFor = Exception.class)
	public void batchSku(Long productId, String flag) {
		Sku sku = new Sku();
		if(flag.equals("1")){
			Product product = new Product();
			product.setId(productId);
			product.setStatus(ProductStatusEnum.DELIST);
			this.entityDao.update(product);
			sku.setProcessStatus(SkuRestController.SKU_VOID);
		}else if(flag.equals("2")) {
			sku.setProcessStatus(SkuRestController.SKU_SHELF_FAILURE);
		}
		Product product = new Product();
		product.setId(productId);
		sku.setProduct(product);
		skuService.batchSku(sku);
	}

	public Product getByMap(Map<String,String> map){
		return  entityDao.getByMap(map);
	}
}

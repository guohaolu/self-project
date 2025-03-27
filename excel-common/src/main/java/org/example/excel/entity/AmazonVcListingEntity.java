package org.example.excel.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("amazon_vc_listing")
public class AmazonVcListingEntity {
    @TableField("store_id")
    private Long storeId;

    @TableField("store_name")
    private String storeName;

    @TableField("store_code")
    private String storeCode;

    @TableField("market_id")
    private Long marketId;

    @TableField("market_code")
    private String marketCode;

    @TableField("asin")
    private String asin;

    @TableField("pic_url")
    private String picUrl;

    @TableField("product_title")
    private String productTitle;

    @TableField("msku_code")
    private String mskuCode;

    @TableField("ean")
    private String ean;

    @TableField("isbn13")
    private String isbn13;

    @TableField("brand_code")
    private String brandCode;

    @TableField("brand_name")
    private String brandName;

    @TableField("manufacturer_code")
    private String manufacturerCode;

    @TableField("model_number")
    private String modelNumber;

    @TableField("parent_asin")
    private String parentAsin;

    @TableField("product_group")
    private String productGroup;

    @TableField("release_date")
    private Date releaseDate;

    @TableField("replenishment_category")
    private String replenishmentCategory;

    @TableField("upc")
    private String upc;

    @TableField("prep_instructions")
    private String prepInstructions;

    @TableField("prep_instructions_performed_by")
    private String prepInstructionsPerformedBy;

    @TableField("sale_price")
    private BigDecimal salePrice;

    @TableField("currency_code")
    private String currencyCode;

    @TableField("supply_price")
    private BigDecimal supplyPrice;

    @TableField("listing_type")
    private String listingType;

    @TableField("listing_id")
    private String listingId;

    @TableField("listing_status")
    private String listingStatus;

    @TableField("sync_price_time")
    private Date syncPriceTime;

    @TableField("sync_detail_time")
    private Date syncDetailTime;

    @TableField("del_flag")
    private String delFlag;

    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField("tenant_id")
    private Long tenantId;
}
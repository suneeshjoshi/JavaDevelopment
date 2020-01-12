package com.suneesh.trading.models.responses;

import com.suneesh.trading.models.requests.AssetIndexRequest;
import com.google.gson.annotations.SerializedName;

/**
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 7/31/2017
 */
public class AssetIndexResponse extends ResponseBase<AssetIndexRequest> {

    @SerializedName("asset_index")
    private AssetIndex[] assetIndex;

    public AssetIndex[] getAssetIndex() {
        return assetIndex;
    }

    public void setAssetIndex(AssetIndex[] assetIndex) {
        this.assetIndex = assetIndex;
    }
}

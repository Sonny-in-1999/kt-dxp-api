package egovframework.kt.dxp.api.domain.mileage.record;

public enum MileageRequestUrl {
    AUTH_TOKEN("/gather/token"),
    MILEAGE_SEARCH("/citizen/mileage");

    String url;

    MileageRequestUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
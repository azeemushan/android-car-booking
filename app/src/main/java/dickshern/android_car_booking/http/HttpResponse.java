package dickshern.android_car_booking.http;

/**
 * Created by dickshern on 06-Sept-18.
 */

public class HttpResponse {
    private Integer httpCode;
    private String response;

    public HttpResponse(Integer inHttpCode, String inResponse) {
        this.httpCode = inHttpCode;
        this.response = inResponse;
    }

    public Integer getHTTPCode() {
        return httpCode;
    }

    public String getResponse() {
        return response;
    }


    @Override
    public String toString() {
        return "CustomHashMap [HTTP Code=" + httpCode + ", Response" + response + "]";
    }
}

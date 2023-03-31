package fr.hedwin.objects;


public class ApiUser {

    private int id;
    private String api_key;
    private int limit_requests;
    private String address;

    public ApiUser(int id, String api_key, int limit_requests, String address) {
        this.id = id;
        this.api_key = api_key;
        this.limit_requests = limit_requests;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getApiKey() {
        return api_key;
    }

    public int getLimitRequests() {
        return limit_requests;
    }

    public String getAddress() {
        return address;
    }
}

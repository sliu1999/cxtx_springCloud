package com.cxtx.auth_service.web.client.thirdparty;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@FeignClient(url = AMapClient.BASE_URL, name = "amapapi")
public interface AMapClient {

    public static final String BASE_URL = "http://restapi.amap.com/";

    //本篇高德服务api接口  参数详解见 高德服务api 地址:https://lbs.amap.com/api/webservice/summary/


    /**
     * 地里编码
     * http://restapi.amap.com/v3/geocode/geo?key=b67ce0096ddc681f99eb0a0b7aade659&address=江宁区三江旅社&city=南京市
     *
     * @param key
     * @param address
     * @param city
     * @return
     */
    @GetMapping("v3/geocode/geo")
    Map getGeo(@RequestParam("key") String key,
               @RequestParam("address") String address,
               @RequestParam("city") String city);

    /**
     * 步行路径规划
     * https://restapi.amap.com/v3/direction/walking?key=b67ce0096ddc681f99eb0a0b7aade659&origin=116.434307,39.90909&destination=116.434446,39.90816
     *
     * @param key
     * @param origin
     * @param destination
     * @return
     */
    @GetMapping("v3/direction/walking")
    Map getRoutePlanWalk(@RequestParam("key") String key,
                         @RequestParam("origin") String origin,
                         @RequestParam("destination") String destination);


    /**
     * 公交路径规划
     * https://restapi.amap.com/v3/direction/transit/integrated?key=b67ce0096ddc681f99eb0a0b7aade659&origin=116.481499,39.990475&destination=116.465063,39.999538&city=北京
     *
     * @param key
     * @param origin
     * @param destination
     * @param city
     * @return
     */
    @GetMapping("v3/direction/transit/integrated")
    Map getRoutePlanBus(@RequestParam("key") String key,
                        @RequestParam("origin") String origin,
                        @RequestParam("destination") String destination,
                        @RequestParam("city") String city);

    /**
     * 驾车路径规划
     * https://restapi.amap.com/v3/direction/driving?key=b67ce0096ddc681f99eb0a0b7aade659&origin=116.481028,39.989643&destination=116.465302,40.004717
     *
     * @param key
     * @param origin
     * @param destination
     * @return
     */
    @GetMapping("v3/direction/driving")
    Map getRoutePlanDrive(@RequestParam("key") String key,
                          @RequestParam("origin") String origin,
                          @RequestParam("destination") String destination,
                          @RequestParam("waypoints") String waypoints,
                          @RequestParam("strategy") String strategy);


    /**
     * 骑行路径规划
     * https://restapi.amap.com/v4/direction/bicycling?key=b67ce0096ddc681f99eb0a0b7aade659&origin=116.434307,39.90909&destination=116.434446,39.90816
     *
     * @param key
     * @param origin
     * @param destination
     * @return
     */
    @GetMapping("v4/direction/bicycling")
    Map getRoutePlanBicycle(@RequestParam("key") String key,
                            @RequestParam("origin") String origin,
                            @RequestParam("destination") String destination);


    /**
     * 距离测量
     * <p>
     * origin
     * 支持100个坐标对，坐标对见用“| ”分隔；经度和纬度用","分隔
     * <p>
     * type
     * 0：直线距离
     * <p>
     * 1：驾车导航距离（仅支持国内坐标）。
     * <p>
     * 必须指出，当为1时会考虑路况，故在不同时间请求返回结果可能不同。
     * <p>
     * 此策略和驾车路径规划接口的 strategy=4策略基本一致，策略为“ 躲避拥堵的路线，但是可能会存在绕路的情况，耗时可能较长 ”
     * <p>
     * 若需要实现高德地图客户端效果，可以考虑使用驾车路径规划接口
     * <p>
     * 3：步行规划距离（仅支持5km之间的距离）
     * https://restapi.amap.com/v3/distance?key=b67ce0096ddc681f99eb0a0b7aade659&origins=116.481028,39.989643|114.481028,39.989643|115.481028,39.989643&destination=114.465302,40.004717&type=1
     *
     * @param key
     * @param origins
     * @param destination
     * @return
     */
    @GetMapping("v3/distance")
    Map getDistance(@RequestParam("key") String key,
                    @RequestParam("origins") String origins,
                    @RequestParam("destination") String destination,
                    @RequestParam("type") String type);


    /**
     * 关键字搜索poi
     * https://restapi.amap.com/v3/place/text?&key=b67ce0096ddc681f99eb0a0b7aade659&keywords=北京大学&city=beijing&offset=20&page=1&extensions=all
     *
     * @param key
     * @param keywords
     * @param city
     * @param offset
     * @param page
     * @param extensions
     * @return
     */
    @GetMapping("v3/place/text")
    Map getPlaceText(@RequestParam("key") String key,
                     @RequestParam("keywords") String keywords,
                     @RequestParam("types") String types,
                     @RequestParam("city") String city,
                     @RequestParam("offset") String offset,
                     @RequestParam("page") String page,
                     @RequestParam("extensions") String extensions);


    /**
     * 周边搜索poi
     * http://restapi.amap.com/v3/place/around?key=085334dc76c21029daec4910c643025b&location=116.473168,39.993015&keywords=&radius=1000&types=011100&offset=20&page=1&extensions=base
     *
     * @param key
     * @param location
     * @param keywords
     * @param radius
     * @param types
     * @param offset
     * @param page
     * @param extensions
     * @return
     */
    @GetMapping("v3/place/around")
    Map getPlaceAround(@RequestParam("key") String key,
                       @RequestParam("location") String location,
                       @RequestParam("keywords") String keywords,
                       @RequestParam("radius") String radius,
                       @RequestParam("types") String types,
                       @RequestParam("offset") String offset,
                       @RequestParam("page") String page,
                       @RequestParam("extensions") String extensions);


    /**
     * 多边形搜索poi
     * http://restapi.amap.com/v3/place/polygon?key=085334dc76c21029daec4910c643025b&offset=20&page=1&extensions=base&polygon=116.460988,40.006919|116.48231,40.007381;116.47516,39.99713|116.472596,39.985227|116.45669,39.984989|116.460988,40.006919&keywords=肯德基&types=050301
     *
     * @param key
     * @param polygon
     * @param keywords
     * @param types
     * @param offset
     * @param page
     * @param extensions
     * @return
     */
    @GetMapping("v3/place/polygon")
    Map getPlacePolygon(@RequestParam("key") String key,
                        @RequestParam("polygon") String polygon,
                        @RequestParam("keywords") String keywords,
                        @RequestParam("types") String types,
                        @RequestParam("offset") String offset,
                        @RequestParam("page") String page,
                        @RequestParam("extensions") String extensions);

    /**
     * 矩形区域交通态势
     * http://restapi.amap.com/v3/traffic/status/rectangle?key=085334dc76c21029daec4910c643025b&rectangle=116.351147,39.966309;116.357134,39.968727
     *
     * @param key
     * @param rectangle
     * @return
     */
    @GetMapping("v3/traffic/status/rectangle")
    Map getTrafficStatusRectangle(@RequestParam("key") String key,
                                  @RequestParam("rectangle") String rectangle);

    /**
     * 圆形区域交通态势
     * http://restapi.amap.com/v3/traffic/status/circle?key=085334dc76c21029daec4910c643025b&location=116.3057764,39.98641364&radius=1500
     *
     * @param key
     * @param location
     * @param radius
     * @return
     */
    @GetMapping("v3/traffic/status/circle")
    Map getTrafficStatusCircle(@RequestParam("key") String key,
                               @RequestParam("location") String location,
                               @RequestParam("radius") String radius);

    /**
     * 指定线路交通态势
     * http://restapi.amap.com/v3/traffic/status/road?key=085334dc76c21029daec4910c643025b&name=北环大道&adcode=440300
     *
     * @param key
     * @param name
     * @param adcode
     * @return
     */
    @GetMapping("v3/traffic/status/road")
    Map getTrafficStatusRoad(@RequestParam("key") String key,
                             @RequestParam("name") String name,
                             @RequestParam("adcode") String adcode);

}

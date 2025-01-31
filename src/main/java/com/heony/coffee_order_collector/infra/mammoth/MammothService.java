package com.heony.coffee_order_collector.infra.mammoth;

import com.heony.coffee_order_collector._common.enums.*;
import generated.jooq.obj.tables.pojos.Menu;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MammothService {

    @Value("${cafe.mammoth.domain}")
    private String DOMAIN;

    @Value("${cafe.mammoth.path.menu-new}")
    private String MENU_NEW_PATH;

    @Value("${cafe.mammoth.path.menu-express}")
    private String MENU_EXPRESS_PATH;

    @Value("${cafe.mammoth.path.menu-mammoth-coffee}")
    private String MENU_MAMMOTH_COFFEE_PATH;

    @Value("${cafe.mammoth.path.details}")
    private String MENU_DETAILS_PATH;

    private static final Pattern GO_VIEW_B_PATTERN = Pattern.compile("goViewB\\((\\d+)\\)");

    // 유효한 패턴을 정의: 단어(숫자 + "oz")
    // 예: HOT(16oz), ICE(20oz) 등
    private static final Pattern TEMPERATURE_PATTERN = Pattern.compile("^([A-Za-z]+)\\(\\d+oz\\)$");

    public List<Menu> crawlingMenus(StoreType storeType) {
        return crawlingMenus(storeType, null);
    }

    public List<Menu> crawlingMenus(StoreType storeType, Long requestedAt) {
        requestedAt = requestedAt==null ? System.currentTimeMillis() : requestedAt;

        String firstHtml = switch (storeType) {
            case MAMMOTH_COFFEE -> DOMAIN + MENU_MAMMOTH_COFFEE_PATH;
            case MAMMOTH_COFFEE_NEW -> DOMAIN + MENU_NEW_PATH;
            case MAMMOTH_EXPRESS -> DOMAIN + MENU_EXPRESS_PATH;
            case null, default -> null;
        };
        if(firstHtml == null) return null;

        List<Menu> menus = new ArrayList<>();
        try{
            Document doc = Jsoup.connect(firstHtml)
                    .userAgent("Mozilla/5.0")
                    .get();
            Elements menuElements;
            if(storeType == StoreType.MAMMOTH_COFFEE_NEW){
                menuElements = doc.select(".con02 .clear li");
                for (Element menuElement : menuElements) {

                    String attr = menuElement.select("a").attr("href");
                    Matcher matcher = GO_VIEW_B_PATTERN.matcher(attr);
                    String menuDetailUrl = null;
                    if(matcher.find()){
                        menuDetailUrl = DOMAIN + MENU_DETAILS_PATH + matcher.group(1);
                        log.debug("서브메뉴  url : " + DOMAIN + MENU_DETAILS_PATH + matcher.group(1));
                    }
                    String temperatures = null;
                    String imageUrl = null;
                    if(menuDetailUrl != null){
                        Document menuDetailDoc = Jsoup.connect(menuDetailUrl).userAgent("Mozilla/5.0").get();
                        Elements columns = menuDetailDoc.select(".i_table table thead th");
                        log.debug("음료 온도(+구분) RAW : " + columns.stream().map(Element::text).toList());
                        List<String> menuTemperatureList = columns.stream().map(Element::text).filter(s -> !s.isBlank()).filter(s -> {
                            Matcher temperatureMatcher = TEMPERATURE_PATTERN.matcher(s);
                            return temperatureMatcher.find();
                        }).map(MammothService::extractWord).toList();
                        log.debug("음료 온도 : " + menuTemperatureList);

                        temperatures = menuTemperatureList.stream().map(s -> MammothMenuTemperature.valueOf(s).menuTemperature().name()).collect(Collectors.joining(","));

                        String imgSrc = menuDetailDoc.selectFirst(".img_wrap img").attr("src");
                        imageUrl = DOMAIN + imgSrc;
                        log.debug("이미지 url : " + imageUrl);
                    }

                    String menuName = menuElement.select(".txt_wrap strong").getFirst().text();
                    log.debug(menuName);
                    log.debug("-----------------");

                    menus.add(
                            new Menu(
                                    null,
                                    storeType.name(),
                                    MenuCategory.MAMMOTH_NEW.name(),
                                    menuName,
                                    StringUtils.isBlank(temperatures) ? null : temperatures,
                                    StringUtils.isBlank(temperatures) ? null : String.join(",", MenuSize.getMammothSizesString()),
                                    requestedAt,
                                    imageUrl
                            )
                    );
                }

            }else{
                Elements categoryElements = doc.select(".sub_tab2 li");
                for (Element category : categoryElements) {
                    String categoryName = category.text();
                    MenuCategory menuCategory = MenuCategory.getByCategoryNameAndStoreType(categoryName, storeType);
                    String categoryParam = category.select("a").attr("href");

                    log.debug("카테고리 : {}, URL : {}", categoryName, categoryParam);

                    Document subDoc = Jsoup.connect(DOMAIN + categoryParam).userAgent("Mozilla/5.0").get();
                    menuElements = subDoc.select(".con02 .clear li");
                    for (Element menuElement : menuElements) {

                        String attr = menuElement.select("a").attr("href");
                        Matcher matcher = GO_VIEW_B_PATTERN.matcher(attr);
                        String menuDetailUrl = null;
                        if(matcher.find()){
                            menuDetailUrl = DOMAIN + MENU_DETAILS_PATH + matcher.group(1);
                            log.debug("서브메뉴  url : " + DOMAIN + MENU_DETAILS_PATH + matcher.group(1));
                        }
                        String temperatures = null;
                        String imageUrl = null;
                        if(menuDetailUrl != null){
                            Document menuDetailDoc = Jsoup.connect(menuDetailUrl).userAgent("Mozilla/5.0").get();
                            Elements columns = menuDetailDoc.select(".i_table table thead th");
                            log.debug("음료 온도(+구분) RAW : " + columns.stream().map(Element::text).toList());
                            List<String> menuTemperatureList = columns.stream().map(Element::text).filter(s -> !s.isBlank()).filter(s -> {
                                Matcher temperatureMatcher = TEMPERATURE_PATTERN.matcher(s);
                                return temperatureMatcher.find();
                            }).map(MammothService::extractWord).toList();
                            log.debug("음료 온도 : " + menuTemperatureList);

                            temperatures = menuTemperatureList.stream().map(s -> MammothMenuTemperature.valueOf(s).menuTemperature().name()).collect(Collectors.joining(","));

                            String imgSrc = menuDetailDoc.selectFirst(".img_wrap img").attr("src");
                            imageUrl = DOMAIN + imgSrc;
                            log.debug("이미지 url : " + imageUrl);
                        }

                        String menuName = menuElement.select(".txt_wrap strong").getFirst().text();
                        log.debug(menuName);
                        log.debug("-----------------");

                        menus.add(
                                new Menu(
                                        null,
                                        storeType.name(),
                                        menuCategory.name(),
                                        menuName,
                                        StringUtils.isBlank(temperatures) ? null : temperatures,
                                        StringUtils.isBlank(temperatures) ? null : String.join(",", MenuSize.getMammothSizesString()),
                                        requestedAt,
                                        imageUrl
                                )
                        );
                    }

                }
            }


        }catch (Exception e) {
            e.printStackTrace();
        }

        return menus.isEmpty()? null : menus;
    }

    private static String extractWord(String str) {
        String[] parts = str.split("\\(");
        if (parts.length > 0) {
            return parts[0];
        } else {
            return null; // 괄호가 없는 경우
        }
    }
}

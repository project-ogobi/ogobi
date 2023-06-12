package site.ogobi.ogobi.boundedContext.goodPlace;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ApiController {

    @GetMapping("/goodplace")
    public String goodPlace(
            @RequestParam(value = "start", defaultValue = "1") String start,
            @RequestParam(value = "end", defaultValue = "10") String end,
            @RequestParam(value = "vo", defaultValue = "") String vo,
            Model model) {

        try {
            StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088"); /* URL */
            urlBuilder.append("/" + URLEncoder.encode("74485173756c6b693637747a6a4d49", "UTF-8")); /* 인증키 */
            urlBuilder.append("/" + URLEncoder.encode("json", "UTF-8")); /* 요청파일타입(xml, xmlf, xls, json) */
            urlBuilder.append("/" + URLEncoder.encode("ListPriceModelStoreService", "UTF-8")); /* 서비스명 (대소문자 구분 필수) */
            urlBuilder.append("/" + URLEncoder.encode(start, "UTF-8")); /* 요청 시작위치 */
            urlBuilder.append("/" + URLEncoder.encode(end, "UTF-8")); /* 요청 종료위치 */

            if (!vo.isBlank()) {
                urlBuilder.append("/" + URLEncoder.encode(vo, "UTF-8")); /* 분류 코드 */
            }

            StoreListResponse response = WebClient.create()
                    .get()
                    .uri(urlBuilder.toString())
                    .retrieve()
                    .bodyToMono(StoreListResponse.class)
                    .block();

            List<StoreData> stores = response.getService().getStoreData();

            model.addAttribute("stores", stores);
            return "goodplace/list";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
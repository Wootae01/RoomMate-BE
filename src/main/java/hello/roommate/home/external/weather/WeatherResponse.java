package hello.roommate.home.external.weather;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class WeatherResponse {
	private Response response;

	@Getter
	@Setter
	public static class Response {
		private Header header;
		private Body body;
	}

	@Getter
	@Setter
	public static class Header {
		private String resultCode;
		private String resultMsg;
	}

	@Getter
	@Setter
	public static class Body {
		private String dataType;
		private Items items;
		private int pageNo;
		private int numOfRows;
		private int totalCount;
	}

	@Getter
	@Setter
	public static class Items {
		private List<Item> item;
	}

	@Getter
	@Setter
	@ToString
	public static class Item {
		private String baseDate;
		private String baseTime;
		private String category;
		private String fcstDate;
		private String fcstTime;
		private String fcstValue;
		private int nx;
		private int ny;
	}
}

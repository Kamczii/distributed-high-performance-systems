package pl.rsww.touroperator.data_extraction;

import org.json.JSONArray;
import org.json.JSONObject;
import pl.rsww.touroperator.data.HotelInfo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class JsonDataExtractor implements DataExtractor{
    private static final String jsonDir = "json/";
    private static final String settingsFileName = jsonDir + "settings.extractor.json";
    private String fileName;

    @Override
    public List<HotelInfo> extract() {
        List<HotelInfo> hotelInfoList = new LinkedList<>();
        Scanner scanner = new Scanner(loadResource(fileName)); //loadResource()
        while (scanner.hasNextLine()) {
            String temp = scanner.nextLine();
            if (temp.isEmpty()) {
                break;
            }
            HotelInfo info = readJson(temp);
            hotelInfoList.add(info);
        }
        scanner.close();
        return hotelInfoList;
    }

    private InputStream loadResource(String name){
        return getClass().getClassLoader().getResourceAsStream(name);
    }

    private void loadSettings(){
        StringBuilder stringBuilder = new StringBuilder();
        Scanner scanner = new Scanner(loadResource(settingsFileName));
        while (scanner.hasNextLine()){
            stringBuilder.append(scanner.nextLine());
        }
        scanner.close();
        JSONObject jsonObject = new JSONObject(stringBuilder.toString());
        String dataFileName = jsonObject.getString("data");
        fileName = jsonDir + dataFileName;
    }


    private List<String> fromList(JSONArray jsonA){
        List<String> strings = new LinkedList<>();
        int i = 0;
        while (true){
            Object temp = jsonA.opt(i);
            if(temp != null){
                String desc = (String) temp;
                strings.add(desc);
            }else{
                break;
            }
            i++;
        }
        return strings;
    }

    public HotelInfo readJson(String text){
        JSONObject jsonObject = new JSONObject(text);
        String hotelName = jsonObject.getString("hotel_name");
        String country = jsonObject.getString("destination_country");
        String region = jsonObject.getString("destination_region");
        String drive = jsonObject.getString("drive");
        JSONArray jsonRooms = jsonObject.getJSONArray("room_list");
        List<String> rooms = fromList(jsonRooms);

        String city = "";
        List<String> departures = new ArrayList<>();
        if(drive.equals(HotelInfo.DRIVE_AIRPLANE)){
            city = jsonObject.getString("destination_airport");
            JSONArray jsonDepartures = jsonObject.getJSONArray("departures");
            departures = fromList(jsonDepartures);
        }

        if(city.contains("(")){
            city = city.split("\\(")[0];
        }

        HotelInfo info = new HotelInfo();
        info.setName(hotelName);
        info.setCountry(country);
        info.setRegion(region);
        info.setDrive(drive);
        info.setCity(city);
        info.setRooms(rooms);
        info.setDepartures(departures);

        return info;
    }

    public JsonDataExtractor(){
        loadSettings();
    }
}

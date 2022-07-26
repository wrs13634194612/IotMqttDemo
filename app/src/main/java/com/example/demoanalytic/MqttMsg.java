package com.example.demoanalytic;

import java.io.Serializable;

public class MqttMsg implements Serializable {
    public String topic;
    public String message;
    public float Pm25_Data;//0-500
    public float Temperature;//0-80
    public float CO2_Data;//0-2000
    public float TVOC_Data;//0-2
    public float HCHO_Data;//0-1
    public float Humidity;//0-100
    public int ModeState;//0关 1监测灯 2有氧灯光
    public int light;//1--6
    public int ScreenStatus;//0关屏 1开屏

    private int Pm25Level;
    private int TemperatureLevel;
    private int CO2Level;
    private int TVOCLevel;
    private int HCHOLevel;
    private int HumidityLevel;
    private int AllLevel;

    private String Pm25LevelText;
    private String TemperatureLevelText;
    private String CO2LevelText;
    private String TVOCLevelText;
    private String HCHOLevelText;
    private String HumidityLevelText;
    private String AllLevelText;
    private boolean isClosed;

    private String clock1;
    private String clock2;
    private String clock3;

    private boolean clock1On;
    private boolean clock2On;
    private boolean clock3On;

    public String plug;

    private static final String TAG = "MqttMsg";

    public String getClock1() {
        return clock1;
    }

    public void setClock1(String clock1) {
        this.clock1 = clock1;
    }

    public String getClock2() {
        return clock2;
    }

    public void setClock2(String clock2) {
        this.clock2 = clock2;
    }

    public String getClock3() {
        return clock3;
    }

    public void setClock3(String clock3) {
        this.clock3 = clock3;
    }

    public boolean isClock1On() {
        return clock1On;
    }

    public void setClock1On(boolean clock1On) {
        this.clock1On = clock1On;
    }

    public boolean isClock2On() {
        return clock2On;
    }

    public void setClock2On(boolean clock2On) {
        this.clock2On = clock2On;
    }

    public boolean isClock3On() {
        return clock3On;
    }

    public void setClock3On(boolean clock3On) {
        this.clock3On = clock3On;
    }

    public MqttMsg(boolean isClosed) {
        this.isClosed = isClosed;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public String equipmentId;
    public String productId;

    public MqttMsg(String topic, String message, String equipmentId) {
        this.topic = topic;
        this.message = message;
        this.equipmentId = equipmentId;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getTopic() {
        return topic;
    }

    public String getMessage() {
        return message;
    }

    public float getPm25_Data() {
        return Pm25_Data;
    }

    public float getTemperature() {
        return Temperature;
    }

    public float getCO2_Data() {
        return CO2_Data;
    }

    public float getTVOC_Data() {
        return TVOC_Data;
    }

    public float getHCHO_Data() {
        return HCHO_Data;
    }

    public float getHumidity() {
        return Humidity;
    }

    public int getPm25Level() {
        if (Pm25_Data <= 35) {
            Pm25Level = 1;
        } else if (Pm25_Data <= 75) {
            Pm25Level = 2;
        } else if (Pm25_Data <= 115) {
            Pm25Level = 3;
        } else if (Pm25_Data <= 150) {
            Pm25Level = 4;
        } else if (Pm25_Data > 150) {
            Pm25Level = 5;
        }
        return Pm25Level;
    }


    public int getCO2Level() {
        if (CO2_Data <= 350 && CO2_Data >= 250) {
            CO2Level = 1;
        } else if (CO2_Data > 350 && CO2_Data <= 1000) {
            CO2Level = 2;
        } else if (CO2_Data > 1000) {
            CO2Level = 3;
        } else if (CO2_Data > 1500) {
            CO2Level = 4;
        } else if (CO2_Data > 2000) {
            CO2Level = 5;
        }
        return CO2Level;
    }

    public int getTVOCLevel() {
        if (TVOC_Data <= 0.3) {
            TVOCLevel = 1;
        } else if (TVOC_Data > 0.3 && TVOC_Data <= 0.6) {
            TVOCLevel = 2;
        } else if (TVOC_Data > 0.6 && TVOC_Data <= 1) {
            TVOCLevel = 3;
        } else if (TVOC_Data > 1 && TVOC_Data <= 1.6) {
            TVOCLevel = 4;
        } else if (TVOC_Data > 1.6) {
            TVOCLevel = 5;
        }
        return TVOCLevel;
    }

    public int getHCHOLevel() {
        if (HCHO_Data <= 0.05) {
            HCHOLevel = 1;
        } else if (HCHO_Data > 0.05 && HCHO_Data <= 0.1) {
            HCHOLevel = 2;
        } else if (HCHO_Data > 0.1 && HCHO_Data <= 0.2) {
            HCHOLevel = 3;
        } else if (HCHO_Data > 0.2 && HCHO_Data <= 0.3) {
            HCHOLevel = 4;
        } else if (HCHO_Data > 0.3) {
            HCHOLevel = 5;
        }
        return HCHOLevel;
    }


    public int getAllLevel() {
        int[] level = new int[4];
        level[0] = getPm25Level();
        level[1] = getCO2Level();
        level[2] = getTVOCLevel();
        level[3] = getHCHOLevel();

        int max = level[0];//将数组的第一个元素赋给max
        int min = level[0];//将数组的第一个元素赋给min
        for (int i = 1; i < level.length; i++) {//从数组的第二个元素开始赋值，依次比较
            if (level[i] > max) {//如果arr[i]大于最大值，就将arr[i]赋给最大值
                max = level[i];
            }
            if (level[i] < min) {//如果arr[i]小于最小值，就将arr[i]赋给最小值
                min = level[i];
            }
        }
        return max;
    }

    public String getPm25LevelText() {
        TemperatureLevelText = "未知";
        if (isClosed) return "...";
        if (Pm25_Data <= 35) {
            Pm25LevelText = "优";
        } else if (Pm25_Data <= 75) {
            Pm25LevelText = "良";
        } else if (Pm25_Data <= 115) {
            Pm25LevelText = "轻度";
        } else if (Pm25_Data <= 150) {
            Pm25LevelText = "中度";
        } else if (Pm25_Data > 150) {
            Pm25LevelText = "中度";
        }
        return Pm25LevelText;
    }

    public String getTemperatureLevelText() {
        if (isClosed) return "...";
        TemperatureLevelText = "未知";
        if (Temperature >= 12 && Temperature <= 24) {
            TemperatureLevelText = "舒适";
        } else if (Temperature >= -10 && Temperature < 12) {
            TemperatureLevelText = "偏冷";
        } else if (Temperature < -12) {
            TemperatureLevelText = "严寒";
        } else if (Temperature <= 38 && Temperature > 24) {
            TemperatureLevelText = "偏热";
        } else if (Temperature > 38) {
            TemperatureLevelText = "炙热";
        }
        return TemperatureLevelText;
    }

    public String getCO2LevelText() {
        if (isClosed) return "...";
        CO2LevelText = "未知";
        if (CO2_Data <= 350) {
            CO2LevelText = "清新";
        } else if (CO2_Data > 350 && CO2_Data <= 1000) {
            CO2LevelText = "较好";
        } else if (CO2_Data > 1000) {
            CO2LevelText = "较浑浊";
        } else if (CO2_Data > 1500) {
            CO2LevelText = "浑浊";
        } else if (CO2_Data > 2000) {
            CO2LevelText = "严重";
        }
        return CO2LevelText;
    }

    public String getTVOCLevelText() {
        if (isClosed) return "...";
        TVOCLevelText = "未知";
        if (TVOC_Data <= 0.3) {
            TVOCLevelText = "较好";
        } else if (TVOC_Data > 0.3 && TVOC_Data <= 0.6) {
            TVOCLevelText = "良";
        } else if (TVOC_Data > 0.6 && TVOC_Data <= 1) {
            TVOCLevelText = "轻度";
        } else if (TVOC_Data > 1 && TVOC_Data <= 1.6) {
            TVOCLevelText = "中度";
        } else if (TVOC_Data > 1.6) {
            TVOCLevelText = "重度";
        }
        return TVOCLevelText;
    }

    public String getHCHOLevelText() {
        if (isClosed) return "...";
        HCHOLevelText = "未知";
        if (HCHO_Data <= 0.05) {
            HCHOLevelText = "优";
        } else if (HCHO_Data > 0.05 && HCHO_Data <= 0.1) {
            HCHOLevelText = "良";
        } else if (HCHO_Data > 0.1 && HCHO_Data <= 0.2) {
            HCHOLevelText = "轻度";
        } else if (HCHO_Data > 0.2 && HCHO_Data <= 0.3) {
            HCHOLevelText = "中度";
        } else if (HCHO_Data > 0.3) {
            HCHOLevelText = "重度";
        }
        return HCHOLevelText;
    }

    public String getAllLevelText() {
        if (isClosed) return "...";
        AllLevelText = "未知";
        int[] level = new int[4];
        level[0] = getPm25Level();
        level[1] = getCO2Level();
        level[2] = getTVOCLevel();
        level[3] = getHCHOLevel();
        int max = level[0];//将数组的第一个元素赋给max
        for (int i = 1; i < level.length; i++) {//从数组的第二个元素开始赋值，依次比较
            if (level[i] > max) {//如果arr[i]大于最大值，就将arr[i]赋给最大值
                max = level[i];
            }
        }
        if (max == 1) {
            AllLevelText = "优";
        } else if (max == 2) {
            AllLevelText = "良";
        } else if (max == 3) {
            AllLevelText = "中";
        } else if (max == 4) {
            AllLevelText = "较差";
        } else if (max == 5) {
            AllLevelText = "差";
        }
        return AllLevelText;
    }

    public String getHumidityLevelText() {
        if (isClosed) return "...";
        if (Humidity <= 30) {
            HumidityLevelText = "偏干";
        } else if (Humidity > 30 && Humidity <= 60) {
            HumidityLevelText = "舒适";
        } else if (Humidity > 60) {
            HumidityLevelText = "偏湿气";
        }
        return HumidityLevelText;
    }

    @Override
    public String toString() {
        return "MqttMsg{" +
                "topic='" + topic + '\'' +
                ", message='" + message + '\'' +
                ", Pm25_Data=" + Pm25_Data +
                ", Temperature=" + Temperature +
                ", CO2_Data=" + CO2_Data +
                ", TVOC_Data=" + TVOC_Data +
                ", HCHO_Data=" + HCHO_Data +
                ", Humidity=" + Humidity +
                ", ModeState=" + ModeState +
                ", light=" + light +
                ", ScreenStatus=" + ScreenStatus +
                ", Pm25Level=" + Pm25Level +
                ", TemperatureLevel=" + TemperatureLevel +
                ", CO2Level=" + CO2Level +
                ", TVOCLevel=" + TVOCLevel +
                ", HCHOLevel=" + HCHOLevel +
                ", HumidityLevel=" + HumidityLevel +
                ", AllLevel=" + AllLevel +
                ", Pm25LevelText='" + Pm25LevelText + '\'' +
                ", TemperatureLevelText='" + TemperatureLevelText + '\'' +
                ", CO2LevelText='" + CO2LevelText + '\'' +
                ", TVOCLevelText='" + TVOCLevelText + '\'' +
                ", HCHOLevelText='" + HCHOLevelText + '\'' +
                ", HumidityLevelText='" + HumidityLevelText + '\'' +
                ", AllLevelText='" + AllLevelText + '\'' +
                ", isClosed=" + isClosed +
                ", clock1='" + clock1 + '\'' +
                ", clock2='" + clock2 + '\'' +
                ", clock3='" + clock3 + '\'' +
                ", clock1On=" + clock1On +
                ", clock2On=" + clock2On +
                ", clock3On=" + clock3On +
                ", plug='" + plug + '\'' +
                ", equipmentId='" + equipmentId + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return equipmentId.length();
    }

    @Override
    public boolean equals(Object obj) {
        return this.equipmentId.equals(((MqttMsg) obj).equipmentId);
    }
}

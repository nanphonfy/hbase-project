package cn.sibat.hbase.take.entity;

/**
 * Created by nanphonfy on 2017/8/16.
 */
public class BusRoad {
    private String name;
    private String we;
    private String ns;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.replaceAll("'","");
    }

    public String getWe() {
        return we;
    }

    public void setWe(String we) {
        this.we = we.replaceAll("'","");
    }

    public String getNs() {
        return ns;
    }

    public void setNs(String ns) {
        this.ns = ns.replaceAll("'","");
    }

    @Override
    public String toString() {
        return "BusRoad{" +
                "name='" + name + '\'' +
                ", we='" + we + '\'' +
                ", ns='" + ns + '\'' +
                '}';
    }
}

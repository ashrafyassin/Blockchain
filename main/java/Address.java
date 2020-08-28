public class Address {
    private String ip;
    private int port;

    Address(String addr) {
        ip = addr.substring(0, addr.indexOf(":"));
        port = Integer.parseInt(addr.substring(addr.indexOf(":") + 1));
    }

    Address(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    String GetIp() {
        return ip;
    }

    int GetPort() {
        return port;
    }

    @Override
    public String toString() {
        return ip + ":" + port;
    }

    @Override
    public boolean equals(Object addr) {
        if (addr == null) {
            return false;
        }
        return ((Address) addr).ip.equals(ip) && ((Address) addr).port == port;
    }
}

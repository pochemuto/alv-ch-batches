package ch.alv.batches.partnerjob.to.master.config;

public class Partner {

    public enum Mode {
        HTTP("HTTP");

        private final String mode;

        Mode(final String mode) {
            this.mode = mode;
        }

        /* (non-Javadoc)
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return mode;
        }
    }

    private String code;
    private String name;
    private Mode mode;
    private String uri;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}

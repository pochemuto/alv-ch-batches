package ch.alv.batches.partnerjob.to.master.config;

public class Partner {

    public enum Mode {
        PROSPECTIVE("prospective"),
        UBS("ubs");

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
    private Mode mode;
    private String uri;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

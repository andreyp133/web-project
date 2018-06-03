package by.gsu.epamlab.model.database;

public enum Sections {
    TODAY {
        private static final String TODAY_QUERY_END =
                "WHERE idUser = ? and date <= CURRENT_DATE() and fixed = false and bined = false;";
        @Override
        protected String getQueryEnd() {
            return TODAY_QUERY_END;
        }
    },
    TOMORROW {
        private static final String TOMORROW_QUERY_END =
                "WHERE idUser = ? and date = CURRENT_DATE() + 1 and fixed = false and bined = false;";
        @Override
        protected String getQueryEnd() {
            return TOMORROW_QUERY_END;
        }
    },
    SOMEDAY {
        private static final String SOMEDAY_QUERY_ENDS =
                "WHERE idUser = ? and date >= CURRENT_DATE() and fixed = false and bined = false;";
        @Override
        protected String getQueryEnd() {
            return SOMEDAY_QUERY_ENDS;
        }
    },
    FIXED {
        private static final String FIXED_QUERY_ENDS =
                "WHERE idUser = ? and fixed = true and bined = false;";
        @Override
        protected String getQueryEnd() {
            return FIXED_QUERY_ENDS;
        }
    },
    BIN {
        private static final String BIN_QUERY_END =
                "WHERE idUser = ? and bined = true;";
        @Override
        protected String getQueryEnd() {
            return BIN_QUERY_END;
        }
    };

    protected abstract String getQueryEnd();

    public static String getQueryEnd(String sec){
        Sections section = Sections.valueOf(sec.toUpperCase());
        return section.getQueryEnd();
    }
}
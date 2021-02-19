class LocalConvert{
      private fun getLocaleFromISO3Country(defaultISO3Country: String): Locale? {
        val locales = Locale.getAvailableLocales()
        for (i in locales.indices) {
            try {
                if (defaultISO3Country == locales[i].isO3Country) {
                    return locales[i]
                }
            } catch (e: Exception) {
                continue
            }
        }
        return null
    }
}

//Java
/*
    private Locale getLocaleFromISO3Country(String defaultISO3Country){
        Locale[] locales = Locale.getAvailableLocales();
        for(int i=0; i<locales.length; i++){
            try{
                if(defaultISO3Country.equals(locales[i].getISO3Country())){
                    return locales[i];
                }
            }
            catch (Exception e){
                continue;
            }
        }
        return null;
    }
*/

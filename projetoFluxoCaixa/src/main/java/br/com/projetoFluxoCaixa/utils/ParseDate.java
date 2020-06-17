package br.com.projetoFluxoCaixa.utils;

import java.util.Date;
import java.util.GregorianCalendar;

public class ParseDate {
	
	public Date getLimiteInicial(Date date, int field) {

        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.set(GregorianCalendar.HOUR_OF_DAY, 0);
        gc.clear(GregorianCalendar.MINUTE);
        gc.clear(GregorianCalendar.SECOND);
        gc.clear(GregorianCalendar.MILLISECOND);
        
        switch (field) {
            case GregorianCalendar.YEAR:
                gc.set(GregorianCalendar.MONTH, 0);
            case GregorianCalendar.MONTH:
                gc.set(GregorianCalendar.DAY_OF_MONTH, 1);
        }
        return gc.getTime();
    }
    
    public Date getLimiteFinal(Date date, int field) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.set(GregorianCalendar.HOUR_OF_DAY, 23);
        gc.set(GregorianCalendar.MINUTE, 59);
        gc.set(GregorianCalendar.SECOND, 59);
        gc.set(GregorianCalendar.MILLISECOND, 999);
        
        switch (field) {
            case GregorianCalendar.YEAR:
                gc.set(GregorianCalendar.MONTH, 11);
            case GregorianCalendar.MONTH:
                gc.set(GregorianCalendar.DAY_OF_MONTH, gc.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
        }
        return gc.getTime();
    }

}

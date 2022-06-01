package br.edu.utfpr.dv.sireata.bo;

import br.edu.utfpr.dv.sireata.dao.DAO;

public abstract class BOFactory {

    public abstract DAO createDAO();

}
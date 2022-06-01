package br.edu.utfpr.dv.sireata.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class DAO<T> {

    public DAO () {}
    public abstract T buscarPorId(int id) throws SQLException;

    public abstract int salvar(T object) throws SQLException;

    public abstract T carregarObjeto(ResultSet rs) throws SQLException;
}

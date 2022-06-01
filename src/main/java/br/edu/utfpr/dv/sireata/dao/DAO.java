package br.edu.utfpr.dv.sireata.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

interface DAO<T> {

    T buscarPorId(int id) throws SQLException;

    int salvar(T object) throws SQLException;

    T carregarObjeto(ResultSet rs) throws SQLException;
}

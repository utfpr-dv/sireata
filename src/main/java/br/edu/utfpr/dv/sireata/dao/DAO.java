package br.edu.utfpr.dv.sireata.dao;

import java.sql.ResultSet;
import java.util.List;

public interface DAO<T> {

    T buscarPorId(int id);

    int salvar(T object);

    void excluir(int id);

    T carregarObjeto(ResultSet rs);

    List<T> listar();

    List<T> listarPorCampus();

    List<T> listarPorAta();

    List<T> listarPorDepartamento();

    List<T> listarParaCriacaoAta();

    List<T> listarParaConsultaAtas();

    List<T> listarTodos(boolean apenasAtivos);

}

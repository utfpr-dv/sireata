package br.edu.utfpr.dv.sireata.util;

public class Querys {

    public static String sqlBuscarAtaPorId() {
       return  "SELECT atas.*, orgaos.nome AS orgao, p.nome AS presidente, s.nome AS secretario " +
                "FROM atas INNER JOIN orgaos ON orgaos.idOrgao=atas.idOrgao " +
                "INNER JOIN departamentos ON departamentos.idDepartamento=orgaos.idDepartamento " +
                "INNER JOIN usuarios p ON p.idUsuario=atas.idPresidente " +
                "INNER JOIN usuarios s ON s.idUsuario=atas.idSecretario " +
                "WHERE idAta = ?";
    }

    public static String sqlBuscarAtaPorNumero() {
        return "SELECT atas.*, orgaos.nome AS orgao, p.nome AS presidente, s.nome AS secretario " +
                "FROM atas INNER JOIN orgaos ON orgaos.idOrgao=atas.idOrgao " +
                "INNER JOIN departamentos ON departamentos.idDepartamento=orgaos.idDepartamento " +
                "INNER JOIN usuarios p ON p.idUsuario=atas.idPresidente " +
                "INNER JOIN usuarios s ON s.idUsuario=atas.idSecretario " +
                "WHERE atas.publicada = 1 AND atas.idOrgao = ? AND atas.tipo = ? AND atas.numero = ? AND YEAR(atas.data) = ?";
    }

    public static String sqlBuscarAtaPorPauta() {
        return "SELECT DISTINCT atas.*, orgaos.nome AS orgao, p.nome AS presidente, s.nome AS secretario " +
                "FROM atas INNER JOIN orgaos ON orgaos.idOrgao=atas.idOrgao " +
                "INNER JOIN departamentos ON departamentos.idDepartamento=orgaos.idDepartamento " +
                "INNER JOIN usuarios p ON p.idUsuario=atas.idPresidente " +
                "INNER JOIN usuarios s ON s.idUsuario=atas.idSecretario " +
                "INNER JOIN pautas ON pautas.idAta=atas.idAta " +
                "WHERE pautas.idPauta = ?";
    }

    public static String sqlBuscarListaAtaPorIdUsuarioIdcampusIddepartamentoIdorgaoSePublicada(int idCampus,
                                                                                               int idDepartamento,
                                                                                               int idOrgao) {
        return "SELECT DISTINCT atas.*, orgaos.nome AS orgao, p.nome AS presidente, s.nome AS secretario " +
                "FROM atas INNER JOIN ataparticipantes ON ataparticipantes.idAta=atas.idAta " +
                "INNER JOIN orgaos ON orgaos.idOrgao=atas.idOrgao " +
                "INNER JOIN departamentos ON departamentos.idDepartamento=orgaos.idDepartamento " +
                "INNER JOIN usuarios p ON p.idUsuario=atas.idPresidente " +
                "INNER JOIN usuarios s ON s.idUsuario=atas.idSecretario " +
                "WHERE ataparticipantes.idUsuario = ? " +
                " AND atas.publicada = ? " +
                (idCampus > 0 ? " AND departamentos.idCampus = " + String.valueOf(idCampus) : "") +
                (idDepartamento > 0 ? " AND departamentos.idDepartamento = " + String.valueOf(idDepartamento) : "") +
                (idOrgao > 0 ? " AND atas.idOrgao = " + String.valueOf(idOrgao) : "") +
                "ORDER BY atas.data DESC";
    }

    public static String sqlBuscarListaAtaPorPublicada() {
        return "SELECT atas.*, orgaos.nome AS orgao, p.nome AS presidente, s.nome AS secretario " +
                "FROM atas INNER JOIN orgaos ON orgaos.idOrgao=atas.idOrgao " +
                "INNER JOIN departamentos ON departamentos.idDepartamento=orgaos.idDepartamento " +
                "INNER JOIN usuarios p ON p.idUsuario=atas.idPresidente " +
                "INNER JOIN usuarios s ON s.idUsuario=atas.idSecretario " +
                "WHERE atas.publicada=1 ORDER BY atas.data DESC";
    }
}

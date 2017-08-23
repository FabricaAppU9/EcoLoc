package br.com.fabappu9.ecoloc.BLL;

import br.com.fabappu9.ecoloc.DTO.UsuarioDto;

/**
 * Created by Geraldo on 21/08/2017.
 */

public class UsuarioBLL {
    public boolean cadastrar(UsuarioDto usuario) {
        return false;
    }

    public boolean editar(UsuarioDto usuario) {
        return false;
    }

    public UsuarioDto login(String usuario, String senha) {
        return new UsuarioDto();
    }

    public boolean logout(UsuarioDto usuario) {
        return false;
    }
}

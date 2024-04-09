package organice.account;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Account", description = "API de Usuário")
public class AccountResource implements AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/accounts/info")
    @Operation(summary = "Rota de Informações da Máquina", description = "Rota de Informações da Máquina utilizada para testar o serviço")
    public ResponseEntity<Map<String, String>> info() {
        return new ResponseEntity<Map<String, String>>(
            Map.ofEntries(
                Map.entry("microservice.name", AccountApplication.class.getSimpleName()),
                Map.entry("os.arch", System.getProperty("os.arch")),
                Map.entry("os.name", System.getProperty("os.name")),
                Map.entry("os.version", System.getProperty("os.version")),
                Map.entry("file.separator", System.getProperty("file.separator")),
                Map.entry("java.class.path", System.getProperty("java.class.path")),
                Map.entry("java.home", System.getProperty("java.home")),
                Map.entry("java.vendor", System.getProperty("java.vendor")),
                Map.entry("java.vendor.url", System.getProperty("java.vendor.url")),
                Map.entry("java.version", System.getProperty("java.version")),
                Map.entry("line.separator", System.getProperty("line.separator")),
                Map.entry("path.separator", System.getProperty("path.separator")),
                Map.entry("user.dir", System.getProperty("user.dir")),
                Map.entry("user.home", System.getProperty("user.home")),
                Map.entry("user.name", System.getProperty("user.name")),
                Map.entry("jar", new java.io.File(
                    AccountApplication.class.getProtectionDomain()
                        .getCodeSource()
                        .getLocation()
                        .getPath()
                    ).toString())
            ), HttpStatus.OK
        );
    }

    @Override
    @Operation(summary = "Criar um novo usuário", description = "Cria um novo usuário e retorna o objeto criado com seu ID.")
    public ResponseEntity<AccountOut> create(AccountIn in) {
        // parser
        Account account = AccountParser.to(in);
        // insert using service
        account = accountService.create(account);
        // return
        return ResponseEntity.created(
            ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(account.id())
                .toUri())
            .body(AccountParser.to(account));
    }

    @Override
    @Operation(summary = "Faz Update de um usuário", description = "Faz Update nos valores de um usuário, não implementado")
    public ResponseEntity<AccountOut> update(String id, AccountIn in) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    @Operation(summary = "Faz login de um usuário", description = "Faz login de um usuário e retorna o token da sessão")
    public ResponseEntity<AccountOut> login(LoginIn in) {
        Account account = accountService.login(in.email(), in.password());
        if (account == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(AccountParser.to(account));
    }

    @Override
    @Operation(summary = "Acessa um usuário", description = "Acessa os valores de um usuário, não contando senhas e informações privilegiadas")
    public ResponseEntity<AccountOut> read(String idUser, String roleUser) {
        final AccountOut account = AccountOut.builder()
            .id(idUser)
            .name(roleUser)
            .build();
        return ResponseEntity.ok(account);
    }
    
}

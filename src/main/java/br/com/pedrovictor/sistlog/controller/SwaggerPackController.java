package br.com.pedrovictor.sistlog.controller;

import br.com.pedrovictor.sistlog.dto.PackDTOs.*;
import br.com.pedrovictor.sistlog.dto.PackDTOs.PackDetails.PackDetailsResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "PackController")
public interface SwaggerPackController {

    @Operation(summary = "Cadastro de pacote", description = "Cria um novo despacho e os registros são armazenados")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pacote criado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Erro interno no servidor\"}")
                    ))
    })
    @PostMapping(value = "/create-package", consumes = "application/json", produces = "application/json")
    ResponseEntity<PackCreateResponseDTO> createPackage(@Valid @RequestBody PackCreateRequestDTO body);


    @Operation(summary = "Atualiza o status do pacote", description = "Atualiza o status de um pacote existente (\"CREATED|IN_TRANSIT|DELIVERED\").")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pacote não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Pacote não encontrado\"}")
                    )),
            @ApiResponse(responseCode = "400", description = "Erro de validação",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Erro de validação\"}")
                    )),
            @ApiResponse(responseCode = "500", description = "Erro interno",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Erro interno\"}")
                    ))
    })
    @PatchMapping(value = "/{id}/update-status", consumes = "application/json", produces = "application/json")
    ResponseEntity<PackUpdateStatusResponseDTO> updatePackageStatus(
            @PathVariable Long id,
            @RequestBody PackUpdateStatusRequestDTO body);


    @Operation(summary = "Consulta de pacote", description = "Consulta detalhes de um pacote existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pacote encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pacote não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Pacote não encontrado\"}")
                    )),
            @ApiResponse(responseCode = "500", description = "Erro interno",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Erro interno\"}")
                    ))
    })
    @GetMapping(value = "/{id}/consult-package", produces = "application/json")
    ResponseEntity<PackDetailsResponseDTO> getPackageDetails(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean includeEvents);


    @Operation(summary = "Consulta de pacotes", description = "Consulta detalhes de uma lista de pacotes existentes.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pacotes encontrados com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PackDetailsResponseDTO.class),
                            examples = @ExampleObject(value = "[{\"id\": 1, \"description\": \"Pacote 1\", \"sender\": \"Remetente 1\", \"recipient\": \"Destinatário 1\", \"status\": \"IN_TRANSIT\", \"createdAt\": \"2021-09-01T00:00:00\", \"updatedAt\": \"2021-09-01T00:00:00\"}, " +
                                    "{\"id\": 2, \"description\": \"Pacote 2\", \"sender\": \"Remetente 2\", \"recipient\": \"Destinatário 2\", \"status\": \"DELIVERED\", \"createdAt\": \"2021-09-01T00:00:00\", \"updatedAt\": \"2021-09-01T00:00:00\"}]")
                    )),
            @ApiResponse(responseCode = "404", description = "Pacotes não encontrados",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Pacotes não encontrados\"}")
                    )),
            @ApiResponse(responseCode = "500", description = "Erro interno",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Erro interno\"}")
                    ))
    })
    @GetMapping(value = "/consult-packages", produces = "application/json")
    ResponseEntity<List<PackDetailsResponseDTO>> getPackages(
            @RequestParam(required = false) List<String> recipients,
            @RequestParam(required = false) List<String> senders);


    @Operation(summary = "Cancelamento de pacote", description = "Cancela um pacote existente que ainda não saiu para entrega (\"CANCELLED\").")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pacote cancelado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pacote não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Pacote não encontrado\"}")
                    )),
            @ApiResponse(responseCode = "400", description = "Erro de validação do payload",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Erro de validação do payload\"}")
                    )),
            @ApiResponse(responseCode = "500", description = "Erro interno",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Erro interno\"}")
                    ))
    })
    @PatchMapping(value = "/{id}/cancel-package", produces = "application/json")
    ResponseEntity<PackCancelledResponseDTO> cancelPackage(@PathVariable Long id);
}

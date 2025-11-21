package br.com.sea3.controller;

import br.com.sea3.dto.ClienteResponseDTO;
import br.com.sea3.service.ClienteService;
import br.com.sea3.dto.ClienteRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "http://localhost:5173")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> criarCliente(@Valid @RequestBody ClienteRequestDTO clienteDTO) {
        ClienteResponseDTO clienteSalvo = clienteService.criarCliente(clienteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo);
    }
    @GetMapping
    public List<ClienteResponseDTO> listarClientes() {
        return clienteService.listarTodosClientes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
        ClienteResponseDTO dto = clienteService.buscarClientePorId(id);
        return ResponseEntity.ok(dto);
    }
}

package com.gedalias.mscartoes.application;

import com.gedalias.mscartoes.domain.Cartao;
import com.gedalias.mscartoes.domain.dto.CartoesPorClienteDTO;
import com.gedalias.mscartoes.domain.dto.CreateCartaoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("cartoes")
public class CartoesController {
    private final CartaoService cartaoService;
    private final ClienteCartaoService clienteCartaoService;

    public CartoesController(CartaoService cartaoService,
                             ClienteCartaoService clienteCartaoService) {
        this.cartaoService = cartaoService;
        this.clienteCartaoService = clienteCartaoService;
    }

    @GetMapping
    public String status() {
        return "ok";
    }

    @PostMapping
    public ResponseEntity<?> createNew(@RequestBody CreateCartaoDTO createCartaoDTO) {
        try {
            cartaoService.save(createCartaoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "message", ex.getMessage()
                ));
        }
    }

    @GetMapping(params = "renda")
    public ResponseEntity<List<Cartao>> getCartoesRendaAte(@RequestParam("renda") Long renda) {
        return ResponseEntity.ok(cartaoService.findCartaoRendaMenorIgual(renda));
    }

    @GetMapping(params = "cpf")
    public ResponseEntity<List<CartoesPorClienteDTO>> getCartoesByCliente(@RequestParam("cpf") String cpf) {
        List<CartoesPorClienteDTO> cartoesPorClienteDTOS =
            clienteCartaoService.findByCpf(cpf)
            .stream().map(CartoesPorClienteDTO::parse)
            .toList();
        return ResponseEntity.ok(cartoesPorClienteDTOS);
    }
}

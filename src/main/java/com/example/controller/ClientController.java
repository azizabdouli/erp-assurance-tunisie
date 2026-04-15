import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    // Create a new client
    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        Client savedClient = clientService.save(client);
        return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
    }

    // Read a client by ID
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Client client = clientService.findById(id);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    // Update a client
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client client) {
        Client updatedClient = clientService.update(id, client);
        return new ResponseEntity<>(updatedClient, HttpStatus.OK);
    }

    // Delete a client
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Search clients
    @GetMapping("/search")
    public ResponseEntity<List<Client>> searchClients(@RequestParam String query) {
        List<Client> clients = clientService.search(query);
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    // KYC validation
    @PostMapping("/{id}/kyc")
    public ResponseEntity<Void> validateKYC(@PathVariable Long id) {
        clientService.validateKYC(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // AML screening
    @PostMapping("/{id}/aml")
    public ResponseEntity<Void> performAMLCheck(@PathVariable Long id) {
        clientService.performAMLCheck(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
package ru.t1.java.demo.service;

import ru.t1.java.demo.dto.ClientDto;
import ru.t1.java.demo.model.Client;

import java.io.IOException;
import java.util.List;

public interface ClientService {
    List<ClientDto> parseJson() throws IOException;

    Client registerClient(Client client);

    List<Client> registerClients(List<Client> clients);

}

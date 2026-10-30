package com.enterprise.client.service;

import com.enterprise.client.dto.ClientRequest;
import com.enterprise.client.dto.ClientResponse;
import java.util.List;

public interface ClientService {

    ClientResponse create(ClientRequest request);

    List<ClientResponse> findAll();

    ClientResponse findById(Long id);

    ClientResponse update(Long id, ClientRequest request);

    void delete(Long id);
}

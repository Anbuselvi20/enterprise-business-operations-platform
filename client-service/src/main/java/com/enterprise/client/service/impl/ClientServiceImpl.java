package com.enterprise.client.service.impl;

import com.enterprise.client.dto.ClientRequest;
import com.enterprise.client.dto.ClientResponse;
import com.enterprise.client.entity.Client;
import com.enterprise.client.exception.DuplicateResourceException;
import com.enterprise.client.exception.ResourceNotFoundException;
import com.enterprise.client.repository.ClientRepository;
import com.enterprise.client.service.ClientService;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public ClientResponse create(ClientRequest request) {
        String email = normalizeEmail(request.email());
        if (clientRepository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateResourceException("Email is already associated with a client.");
        }

        Client client = new Client();
        applyRequest(client, request, email);
        return toResponse(clientRepository.saveAndFlush(client));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientResponse> findAll() {
        return clientRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClientResponse findById(Long id) {
        return toResponse(getClient(id));
    }

    @Override
    public ClientResponse update(Long id, ClientRequest request) {
        Client client = getClient(id);
        String email = normalizeEmail(request.email());
        if (clientRepository.existsByEmailIgnoreCaseAndIdNot(email, id)) {
            throw new DuplicateResourceException("Email is already associated with a client.");
        }

        applyRequest(client, request, email);
        return toResponse(clientRepository.saveAndFlush(client));
    }

    @Override
    public void delete(Long id) {
        clientRepository.delete(getClient(id));
    }

    private Client getClient(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Client not found with id: " + id));
    }

    private void applyRequest(Client client, ClientRequest request, String email) {
        client.setClientName(request.clientName().trim());
        client.setIndustry(request.industry().trim());
        client.setContactPerson(request.contactPerson().trim());
        client.setEmail(email);
        client.setPhoneNumber(request.phoneNumber() == null
                ? null
                : request.phoneNumber().trim());
        client.setCountry(request.country().trim());
        client.setStatus(request.status());
    }

    private ClientResponse toResponse(Client client) {
        return new ClientResponse(
                client.getId(),
                client.getClientName(),
                client.getIndustry(),
                client.getContactPerson(),
                client.getEmail(),
                client.getPhoneNumber(),
                client.getCountry(),
                client.getStatus());
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}

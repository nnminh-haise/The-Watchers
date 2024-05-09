package com.example.watch_selling.service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.watch_selling.dtos.CreateInvoiceDto;
import com.example.watch_selling.dtos.EmailInvoiceInformation;
import com.example.watch_selling.dtos.InvoiceDetail;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.mailing.EmailServiceImpl;
import com.example.watch_selling.model.Invoice;
import com.example.watch_selling.model.Order;
import com.example.watch_selling.model.OrderDetail;
import com.example.watch_selling.repository.InvoiceRepository;
import com.example.watch_selling.repository.OrderDetailRepository;
import com.example.watch_selling.repository.OrderRepository;

@Service
public class InvoiceService {
    private final double TAX_PERCENT = 1.08;

    private InvoiceRepository invoiceRepository;

    private OrderRepository orderRepository;

    private OrderDetailRepository orderDetailRepository;

    private EmailServiceImpl emailServiceImpl;

    public InvoiceService(
            InvoiceRepository invoiceRepository,
            OrderRepository orderRepository,
            OrderDetailRepository orderDetailRepository,
            EmailServiceImpl emailServiceImpl) {
        this.invoiceRepository = invoiceRepository;
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.emailServiceImpl = emailServiceImpl;
    }

    public ResponseDto<List<InvoiceDetail>> findAllInvoiceByAccountId(
            Integer page, Integer size,
            String dateSortBy, String fromDate, String toDate,
            String totalSortBy, String fromTotal, String toTotal,
            UUID accountId) {
        ResponseDto<List<InvoiceDetail>> res = new ResponseDto<>(
                null, "", HttpStatus.BAD_REQUEST);

        List<String> sortByValues = List.of("asc", "desc");
        Boolean flag = false;
        for (String value : sortByValues) {
            if (dateSortBy.equalsIgnoreCase(value)) {
                flag = true;
            }
        }
        if (!flag) {
            return res.setMessage("Invalid date sort by value!");
        }

        try {
            LocalDate validFromDate = (fromDate == null) ? null : LocalDate.parse(fromDate);
            LocalDate validToDate = (toDate == null) ? null : LocalDate.parse(toDate);
            Double validFromTotal = (fromTotal == null) ? null : Double.parseDouble(fromTotal);
            Double validToTotal = (toTotal == null) ? null : Double.parseDouble(toTotal);

            Pageable paging = PageRequest.of(page, size);
            List<Invoice> invoices = invoiceRepository.findAllByDateAndTotal(
                    paging, dateSortBy, validFromDate, validToDate, totalSortBy, validFromTotal, validToTotal,
                    accountId);

            if (invoices.isEmpty()) {
                return res
                        .setMessage("Cannot find any invoice!")
                        .setStatus(HttpStatus.NOT_FOUND);
            }

            List<InvoiceDetail> invoiceDetails = new ArrayList<>();

            for (var invoice : invoices) {
                Order order = invoice.getOrder();
                var invoideDetail = createInvoiceDetail(invoice, order);
                if (invoideDetail.isEmpty()) {
                    return res
                            .setStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                            .setMessage("Cannot create invoice detail!");
                }
                invoiceDetails.add(invoideDetail.get());
            }

            return res
                    .setData(invoiceDetails)
                    .setMessage("Invoice found successfully!")
                    .setStatus(HttpStatus.OK);
        } catch (Exception e) {
            return res.setMessage(e.getMessage());
        }
    }

    // public ResponseDto<List<InvoiceDetail>> findAllByAccountId(
    // Integer page, Integer size,
    // String dateSortBy, String fromDate, String toDate,
    // String totalSortBy, String fromTotal, String toTotal,
    // UUID accountId) {
    // // ResponseDto<List<InvoiceDetail>>
    // }

    public ResponseDto<InvoiceDetail> findById(UUID id) {
        ResponseDto<InvoiceDetail> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (id == null) {
            return res.setMessage("Invalid ID!");
        }

        Optional<Invoice> invoice = invoiceRepository.findById(id);
        if (!invoice.isPresent()) {
            return res
                    .setMessage("Invoice not found!")
                    .setStatus(HttpStatus.NOT_FOUND);
        }

        Optional<InvoiceDetail> invoiceDetail = this.createInvoiceDetail(
                invoice.get(), invoice.get().getOrder());
        if (invoiceDetail.isEmpty()) {
            return res
                    .setStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .setMessage("Cannot create invoice detail!");
        }

        return res
                .setMessage("Success!")
                .setStatus(HttpStatus.OK)
                .setData(invoiceDetail.get());
    }

    public ResponseDto<InvoiceDetail> createNewInvoice(CreateInvoiceDto dto) {
        ResponseDto<InvoiceDetail> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (!CreateInvoiceDto.validateDto(dto)) {
            return res.setMessage("Invalid DTO");
        }

        Optional<Order> targetingOrder = orderRepository.findById(dto.getOrderId());
        if (!targetingOrder.isPresent()) {
            return res.setMessage("Cannot find any Order with the given order ID!");
        }

        Double totalPrice = orderDetailRepository.totalOfOrder(dto.getOrderId());
        Double priceAfterTax = totalPrice * TAX_PERCENT;
        LocalDate createDate = LocalDate.now();
        Invoice invoice = invoiceRepository.save(new Invoice(
                null,
                createDate,
                totalPrice,
                dto.getTaxCode(),
                false,
                targetingOrder.get()));

        Optional<InvoiceDetail> invoiceDetail = createInvoiceDetail(invoice, targetingOrder.get());
        if (invoiceDetail.isEmpty()) {
            return res
                    .setStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .setMessage("Cannot create invoice detal!");
        }

        // * Sending email to recevier
        // TODO: improve this code
        String recipient = targetingOrder.get().getAccount().getEmail();
        String subject = "Watch ordering invoice - Number: " + invoice.getId().toString();
        EmailInvoiceInformation body = new EmailInvoiceInformation();
        body.setInvoiceNumber(invoice.getId().toString());
        body.setOwnerEmail(recipient);
        body.setOrderId(targetingOrder.get().getId().toString());
        body.setTotal(EmailInvoiceInformation.formatAsVND(totalPrice));
        body.setTotalAfterTax(EmailInvoiceInformation.formatAsVND(priceAfterTax));
        body.setTaxCode(dto.getTaxCode());
        body.setCreateDate(invoice.getCreateDate());
        body.setContact(this.emailServiceImpl.getSendingEmail());
        ResponseDto<Boolean> emailSender = this.emailServiceImpl.sendEmail(recipient, subject, body.toHtmlBody());
        if (emailSender.getData() == false) {
            return res
                    .setStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .setMessage("Cannot send Email!");
        }

        return res
                .setData(invoiceDetail.get())
                .setStatus(HttpStatus.OK)
                .setMessage("Success!");
    }

    public ResponseDto<String> updateDeleteStatusById(UUID id, Boolean status) {
        ResponseDto<String> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (id == null) {
            return res.setMessage("Invalid ID!");
        }

        invoiceRepository.updateDeleteStatusById(id, status);
        return res
                .setStatus(HttpStatus.OK)
                .setMessage("Invoice updated successfully!");
    }

    private Optional<InvoiceDetail> createInvoiceDetail(Invoice invoice, Order order) {
        if (invoice == null || order == null) {
            return Optional.empty();
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        InvoiceDetail invoiceDetail = new InvoiceDetail();
        invoiceDetail.setOrder(order);
        invoiceDetail.setCreateDate(invoice.getCreateDate().format(dateTimeFormat));
        invoiceDetail.setInvoiceNumber(this.getTimestamp(LocalDateTime.now()));
        invoiceDetail.setTotalPrice(decimalFormat.format(invoice.getTotal()));
        invoiceDetail.setTotalPriceAfterTax(decimalFormat.format(invoice.getTotal() * TAX_PERCENT));
        invoiceDetail.setTaxCode(invoice.getTaxCode());
        invoiceDetail.setReceiverName(order.getName());
        invoiceDetail.setReceiverAddress(order.getAddress());
        invoiceDetail.setReceiverPhoneNumber(order.getPhoneNumber());
        invoiceDetail.setOrderDate(order.getOrderDate().toString());
        invoiceDetail.setEstimateDeliveryDate(order.getDeliveryDate().toString());
        List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrderId(order.getId());
        invoiceDetail.setOrderDetails(orderDetails);

        return Optional.of(invoiceDetail);
    }

    private String getTimestamp(LocalDateTime date) {
        return String.valueOf(date.getYear()) +
                date.getMonthValue() +
                date.getDayOfMonth() +
                date.getHour() +
                date.getMinute() +
                date.getSecond();
    }
}

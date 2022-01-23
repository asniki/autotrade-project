
https://stackabuse.com/serialize-and-deserialize-xml-in-java-with-jackson/
- Serialize and Deserialize XML in Java with Jackson

## Transaq XML Connector command coverage
1. [x] connect (1)
   - authentication
   - markets (4)
   - boards (18)
   - candlekinds (5)
   - securities (6)
   - pits (19)
   - sec_info_upd (8)
   - client (3)
   - positions (14)
   - overnight
   - messages (20)
   - server_status (2)
   - error (21)
   - trades (13)

2. [x] disconnect (2)
   - server_status (2)

3. [x] server_status (3)
   - server_status (2)

4. [ ] subscribe (4)
   - quotations (9)
   - alltrades (10)
   - quotes (11)

5. [ ] unsubscribe (5)
 
6. [ ] get_history_data (6)
   - candles (1)

7. [ ] neworder (7)
   - order (12)
   - trades (13)

8. [ ] newcondorder (8)
   - order (12)

9. [ ] newstoporder (9)
    - order (12)

10. [ ] cancelorder (10)

11. [ ] cancelstoporder (11)

12. [ ] get_forts_position (12)
    - positions (14)

13. [ ] get_client_limits (13)
    - clientlimits (15)

14. [x] get_markets (14)
    - markets (4)

15. [x] get_servtime_difference (15)

16. [ ] change_pass (16)

17. [ ] subscribe_ticks (17)
    - ticks (17)

18. [x] get_connector_version (18)
    - connector_version

19. [x] get_server_id (19)
    - current_server

20. [ ] get_securities_info (20)
    - sec_info (7)

21. [ ] moveorder (21)

22. [ ] get_max_buy_sell_tplus (22)

23. [ ] get_united_equity (23)
    - united_equity (22)

24. [ ] get_united_go (24)
    - united_go (23)

25. [ ] get_mc_portfolio (25)
    - mc_portfolio (26)

26. [ ] get_max_buy_sell (26)
    - max_buy_sell (27)

27. [ ] get_cln_sec_permissions (27)
    - cln_sec_permissions (25)

28. [x] get_securities (28)
    - securities (6)

29. [x] get_old_news (29)
    - news_header

30. [x] get_news_body (30)
    - news_body

- marketord (16)
- union (24)
DELETE FROM base.result_code_message WHERE result_code_id = (SELECT id FROM base.result_code WHERE key = '227');
DELETE FROM base.result_code WHERE key = '227';


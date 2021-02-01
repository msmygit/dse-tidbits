# Exracted from `sperf` at https://github.com/DataStax-Toolkit/sperf/blob/master/pysper/parser/cfstats.py
import re
from collections import OrderedDict
#from pysper import diag

class Parser:
    """parses CFStats"""

    def __init__(self):
        self.parsed = OrderedDict()
        self.current_table = ""
        self.current_keyspace = ""
        self.rules = [
            {
                "m": re.compile(r"Keyspace : (?P<keyspace_name>[^\n]+)"),
                "f": self._keyspace_match,
            },
            {
                "m": re.compile(r"Keyspace: (?P<keyspace_name>[^\n]+)"),
                "f": self._keyspace_match,
            },
            {
                "m": re.compile(r"\t\tTable: (?P<table_name>[^\n]+)"),
                "f": self._table_match,
            },
            {
                "m": re.compile(r"\t\tTable \(index\): (?P<table_name>[^\n]+)"),
                "f": self._table_match,
            },
            {
                "m": re.compile(r"\t\t(?P<key>.+): (?P<value>\d+\.\d+)"),
                "f": self._table_stat_float_match,
            },
            {
                "m": re.compile(r"\t\t(?P<key>.+): (?P<value>NaN)"),
                "f": self._table_stat_float_match,
            },
            {
                "m": re.compile(r"\t\t(?P<key>.+): (?P<value>\d+)"),
                "f": self._table_stat_int_match,
            },
            {
                "m": re.compile(r"\t\t(?P<key>.+): (?P<value>\w+)"),
                "f": self._table_stat_regex,
            },
        ]

    def _keyspace_match(self, match):
        self.current_keyspace = match.group("keyspace_name")
        self.parsed[self.current_keyspace] = OrderedDict()

    def _table_match(self, match):
        self.current_table = match.group("table_name")
        self.parsed[self.current_keyspace][self.current_table] = OrderedDict()

    def _table_stat_float_match(self, match):
        key = match.group("key")
        value = match.group("value")
        self.parsed[self.current_keyspace][self.current_table][key] = float(value)

    def _table_stat_int_match(self, match):
        key = match.group("key")
        value = match.group("value")
        self.parsed[self.current_keyspace][self.current_table][key] = int(value)

    def _table_stat_regex(self, match):
        key = match.group("key")
        value = match.group("value")
        self.parsed[self.current_keyspace][self.current_table][key] = value

    def capture_line(self, line):
        """matches keyspace, tables and their stats"""
        for rule in self.rules:
            match = rule["m"].match(line)
            if match:
                rule["f"](match)
                return
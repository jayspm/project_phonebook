using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using project_phonebook.Models;
using Microsoft.EntityFrameworkCore;

namespace project_phonebook.DbContexts
{
    public class PhonebookDbContext : DbContext
    {
        public PhonebookDbContext(DbContextOptions<PhonebookDbContext> options) 
            : base(options) { }
        public DbSet<Contact> Contacts { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Contact>().ToTable("Contact");
            modelBuilder.Entity<Contact>()
                .HasKey(contact => contact.Id);
        }
    }
}
